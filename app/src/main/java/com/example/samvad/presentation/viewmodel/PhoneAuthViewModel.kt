package com.example.samvad.presentation.viewmodel

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.samvad.models.PhoneAuthUser
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.logging.Log
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit


import javax.inject.Inject
import kotlin.io.encoding.Base64

@HiltViewModel   // isse kya hoga is hmara jo class hai uska object hmlogo ko hilt bna ke de dega
class PhoneAuthViewModel @Inject constructor(
    private val firebaseAuth:FirebaseAuth,  //Firebase authentication access
    private val database: FirebaseDatabase  //Firebase database access
):ViewModel() {

    //authentication ka state reperesent krne ke liye ye variable bna rhe h ....isse hme ye pta chlega ki user login hua ki nhi hua yaa phir koi error hai  aur yaa to loading state me hai
    //toh hmlog authentication ke state ko represent krne ke liye ye _authState variable bnaa rhe hai.
    private val _authState = MutableStateFlow<AuthState>(AuthState.Ideal)
    //Ideal means do nothing  ... hmlogo ne AuthState.Ideal isiliye rkha hai kyoki starting me authentication ka jo state hai usme kuch bhi nhi hone wala hai isliye abhi hmlogo ne Auth.Ideal rkha hai

    //ye wala variable UI me expose hoga  --> public variable
    val authState = _authState.asStateFlow()

    //ab hm database ka reference create krenge --> reference ka mtlv ye hai ki jitna bhi detail  hai wo database me jaa ke save hone wala hai wo khaa pe save hoga database me ...toh jo nodes hai unka name dene waale hai....unhi nodes me jaa ke details save honge user ke database me
    //now we will see node creation

    private val userRef = database.reference.child("users")  // isse database me ek folder create hua ...aur uss folder me user ka jitna bhi detail hoga wo jaa ke save ho jaaega


    //function of sendVerficationCode --> it will send the verification code
    fun sendVerificationCode(phoneNumber: String , activity: Activity){  // iss function kaa kaam h user ke pgone number pe OTP bhejna
        //Starting state ideal state hoga , aur jb user apna Phone number daalega and "Get OTP" button pe click krega tb ye function run krna chahiye
        //jb bhi ye function run hoga toh hmlogo ka authentication ka state "ideal" se "loading" state me chla jaaega

        _authState.value = AuthState.Loading  //going to the loading state
        //Here, we are applying the firebase callback system
        val option = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){  //PhoneAuthProvider class is provided by firebase

            //agr OTP successfully bhej diya gya hai toh ye wala callback function chl jaaega
            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(id, token)
                android.util.Log.d("PhoneAuth"  , "onCodeSent triggered. verification ID: $id")
                _authState.value = AuthState.CodeSent(verificationId = id)
            }
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                 signWithCredential(credential , context = activity)
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                android.util.Log.e("PhoneAuth" , "Verification failed: ${exception.message}")
                _authState.value = AuthState.Error(exception.message?:"Verification Failed")
            }

        }

        //iss code ka kaam h actual OTP send krna.... yhaa pe hm phoneAuthOptions ka object create kr rhe hai jo firebase ko btata hai ki OTP bhejne ke liye kya kyaa chij chahiye
        val phoneAuthOptions = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber) //wo phoneNumber set kiya jata hai jispe OTP bhejna h
            .setTimeout(60L , TimeUnit.SECONDS)// OTP bhejne ke baad 60 seconds tk wait kre
            .setActivity(activity) //current screen ka reference de rhe hai jisme OTP process ka UI chl rha hoga
            .setCallbacks(option) // isme callbacks ko dekha jata h
            .build()

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)  // firebase ko bola jata hai ki OTP bhej do.....ab yhaa pe firebase backend me jaaega aur given number pe OTP bhej dega  .....agrr sb kuch shi rhaa toh "onCodeSent" function call hoga aur agrr koi error aaya toh "onVerificationFailed" function call hoga

    }

    private fun signWithCredential(credential: PhoneAuthCredential , context: Context){
            _authState.value = AuthState.Loading  //loading state ..mtlv authentication process

            firebaseAuth.signInWithCredential(credential)  // verification process
                .addOnCompleteListener{task ->   //Here , task is the explicit parameter

                    if(task.isSuccessful){
                            //task successful hone ke baad user ko store krenge
                        val user = firebaseAuth.currentUser
                        val phoneAuthUser = PhoneAuthUser(
                            userId = user?.uid?:"" ,
                            phoneNumber = user?.phoneNumber?: ""



                        )
                        //user jb second time app open kre toh use registration screen nhi dikhna chahiye
                        markUserAsSignedIn(context)  // ye function mark kr dega ki particular user registered ho gyaa h ...aur jb wo next time screen open kre toh usee registration screen nhi dikhe
                        _authState.value = AuthState.Success(phoneAuthUser)


                        fetchUserProfile(user?.uid?:"")  //it will fetch the data of user like name and profile pic etc.
                    }else{
                        _authState.value = AuthState.Error(task.exception?.message?:"Sign-in failed")
                    }
                }
    }

    private fun markUserAsSignedIn(context: Context){
        //Context parameter app ke environment ,state banata hai aur hme app ka koi data use krna hai to hm context ke jariye use kr skte hai

        //sharedPreference ek storage hota hai app ke andrr jisme key value pair me data save hota hai
        val sharedPreference = context.getSharedPreferences("app_prefs" , Context.MODE_PRIVATE) //Context.MODE_PRIVATE -> data kisi aur app ke saath share nh hoga kewal issi app ke pass rhega
        sharedPreference.edit().putBoolean("isSignedIn" , true).apply()
    }

    private fun fetchUserProfile(userId:String){

        val userRef = userRef.child(userId)  // node access ho rha h database se
        userRef.get().addOnSuccessListener { snapshot ->

            if(snapshot.exists()){  //database me data exist kr rha hai ki nhi? isiliye if run kraa

                val userProfile = snapshot.getValue(PhoneAuthUser::class.java)  // converting the accessed data into data class
                if(userProfile != null){

                    _authState.value = AuthState.Success(userProfile)
                }
            }
        }.addOnFailureListener{
            _authState.value = AuthState.Error("Failed to fetch user profile")
        }
    }

        //ye OTP ko verify krega
    fun verifyCode(otp: String , context: Context){
        //currentAuthState variable _authState.value ka reference hold krke rkha hai
        val currentAuthState = _authState.value

        if(currentAuthState !is AuthState.CodeSent || currentAuthState.verificationId.isEmpty()){
            android.util.Log.e("PhoneAuth" , "Attempting to verify OTP without a valid verification ID")

            _authState.value = AuthState.Error("Verification not started or Invalid ID")
            return
        }

        val credential = PhoneAuthProvider.getCredential(currentAuthState.verificationId , otp)
        signWithCredential(credential , context)
    }

    fun saveUserProfile(userId: String , name: String ,status: String , profileImage: Bitmap? ){

        val database = FirebaseDatabase.getInstance().reference //database instance hai yeh
        val encodedImage = profileImage?.let{convertBitmapToBase64(it)}  //for profileImage  -- image ko Base64 me convert krte h
        val userProfile = PhoneAuthUser(
            userId = userId ,
            name = name ,
            status = status,
            phoneNumber = Firebase.auth.currentUser?.phoneNumber?:"" ,

            profileImage = encodedImage ,
        )

        database.child("users").child(userId).setValue(userProfile)
    }

    private fun convertBitmapToBase64(bitmap: Bitmap):String{

        val byteArrayOutputStream = ByteArrayOutputStream()  //image data ko byte form me store krega
        bitmap.compress(Bitmap.CompressFormat.JPEG , 100 , byteArrayOutputStream) //bitmap image ko JPEG format me compress kr deta h
        val byteArray = byteArrayOutputStream.toByteArray() //image ab raw data bn gya h
        return android.util.Base64.encodeToString(byteArray , android.util.Base64.DEFAULT)  //Base64 is standard way to represent the binary data into text
    }

        //Photo ko hmlog firebase me directly save nhi kr skte hai ..phele kr skte the but ab firebase charge kr rha hai photo ko save krne ke liye
        //to hmlog JPEG image ko string me convert krke firbase me save krte hai , phir usko reverse krke profile image me show kr dete h

    fun resetAuthState(){
        //ye function authstate ke value ko reset kr dega aur usko ideal pe kr dega
        _authState.value = AuthState.Ideal
    }


    fun signOut(activity: Activity){
        firebaseAuth.signOut()
        val sharedPreference = activity.getSharedPreferences("app_prefs" , Activity.MODE_PRIVATE)
        sharedPreference.edit().putBoolean("isSigned" , false).apply()
    }

}
//Authentication ke events fixed hai isiliye hmlog sealed class ka use kr rhe hai
sealed class AuthState{
      //iss sealed class me authentication ke saare state represent kiya hua hai...
    object Ideal: AuthState()  // do nothing
    object Loading: AuthState()  //loading state --> data aa rha hoga ya jaa rha hoga
    data class CodeSent(val verificationId: String): AuthState() // iss state me jb user apna phone number dega uske baad jb get otp pe click krega ..toh otp send hona chahiye uske liye ye state hai ...isme hmlog user ka verificationId as a parameter lene waale hai
    data class Success(val user: PhoneAuthUser): AuthState() //Successfully authentication puraa hoga toh ye state hoga
    //PhoneAuthUser data class me jb user ka authentication process successful ho jaaega tb hme uss user ka Id , phoneNumber ,name , status , profileImage ye sb backend me firebase ke database me store krna hoga isiliye hmne ek data class banaya PhoneAuthUser jisme chije store ho
    data class Error(val message:String) : AuthState() //Error state ...if there is any problem like network error
}