package com.example.samvad.presentation.profile

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.samvad.R
import com.example.samvad.presentation.navigation.Routes
import com.example.samvad.presentation.viewmodel.PhoneAuthViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun UserProfileSetScreen(
    phoneAuthViewModel: PhoneAuthViewModel = hiltViewModel(),
    navHostController: NavHostController
) {

    var name by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }  //It will store the Uri of Image
//    //URI --> URI ka full form hota hai Uniform Resource Identifier. Yeh ek string hoti hai jo internet ya network par kisi resource (jaise webpage, file, ya service) ko uniquely identify karti hai. URI ka use mainly resources ko locate ya access karne ke liye hota hai.
//    //Agar aap Android development ki baat kar rahe hain (jaise aapke previous questions se lagta hai), toh Uri ek class hoti hai (android.net.Uri) jo content providers se data access karne ke liye use hoti hai.
//    //Yeh Uri content provider se data fetch karne ke liye use hota hai, jaise contacts, images, ya files.
//
//
    var bitmapImage by remember { mutableStateOf<Bitmap?>(null) }
//
//    //Instance of firebaseauth
    val firebaseAuth = Firebase.auth
    val phoneNumber = firebaseAuth.currentUser?.phoneNumber
        ?: ""  //currentuser le rhe hai ...agrr current user hai toh phone number add kr rhe hai aur agr nhi hai toh empty string lenge
    val userId = firebaseAuth.currentUser?.uid ?: ""

    val context = LocalContext.current

    //What we are doing --> hmlog image le rhe hai aur uss image ko string me convert krke firebase ke database me save kr rhe hai aur phir jb uss
    // image ko show krna hai app me toh uss string form me stored image ko access krke usko string se image form me convert kr denge

    //ye imagePicker ....image pick krne ke liye gallery open kr dega aur hm images ko select kr skte hai
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->     //gettig the uri

            profileImageUri = uri

            uri?.let {
                try {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    bitmapImage = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                        decoder.isMutableRequired = true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Handle error (e.g., show a toast or log it)
                }
            }
        }
    )

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(128.dp)
                .clip(shape = CircleShape)
                .border(2.dp, color = Color.Gray, shape = CircleShape)
                .clickable { imagePickerLauncher.launch("image/*") }) {
            if (bitmapImage != null) {
                Image(
                    bitmap = bitmapImage!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else if(profileImageUri != null){
                Image(
                    painter = rememberImagePainter(profileImageUri) ,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = CircleShape),
                    contentScale = ContentScale.Crop
                )
            }



            else {
                    Image(
                        painterResource(id = R.drawable.portraitplaceholder),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )
                    }

        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "$phoneNumber")

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name ,
            onValueChange = {name  = it} ,
            label = {Text("Name")} ,

            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent ,
                focusedContainerColor = Color.Transparent ,
                focusedIndicatorColor = colorResource(R.color.light_green) ,

            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = status ,
            onValueChange = {status  = it} ,
            label = {Text("Name")} ,

            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent ,
                focusedContainerColor = Color.Transparent ,
                focusedIndicatorColor = colorResource(R.color.light_green) ,

                )
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick  = {
            phoneAuthViewModel.saveUserProfile(userId, name,status , bitmapImage)
            navHostController.navigate(Routes.HomeScreen)

        } , colors = ButtonDefaults.buttonColors(colorResource(R.color.light_green))){
                Text("Save")
        }
    }


}