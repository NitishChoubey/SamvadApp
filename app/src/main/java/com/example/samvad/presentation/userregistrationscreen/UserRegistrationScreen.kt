package com.example.samvad.presentation.userregistrationscreen


import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown

import androidx.compose.material3.Icon
import androidx.compose.material.Text

import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.samvad.R
import com.example.samvad.presentation.navigation.Routes
import com.example.samvad.presentation.viewmodel.AuthState
import com.example.samvad.presentation.viewmodel.PhoneAuthViewModel

@Composable

fun UserRegistrationScreen(
    navController: NavHostController,
    phoneAuthViewModel: PhoneAuthViewModel = hiltViewModel()
) {

    var expanded by remember { mutableStateOf(false) }  //ye variable countries ke dropdown menu ko track krega
    var selectedCountry by remember { mutableStateOf("Japan") }  // ye user ne konsa country select kraa h usko store krke rkhega
    var countryCode by remember { mutableStateOf("+81") }
    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }

    val authState by phoneAuthViewModel.authState.collectAsState()
    val context = LocalContext.current
      //Local context ka use krke current context le rhe hai  aur usse Activity me typecast kr rhe hai
      // Helper function to safely get Activity from Context
      fun getActivity(context: Context): Activity? {
          var currentContext = context
          while (currentContext is ContextWrapper) {
              if (currentContext is Activity) {
                  return currentContext
              }
              currentContext = currentContext.baseContext
          }
          return null
      }

    val activity = getActivity(context)  // Safely retrieve the Activity


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Enter your phone number",
            fontSize = 20.sp,
            color = colorResource(id = R.color.dark_green),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text(text = "Samvad will need to verify your phone number.")
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "What's", color = colorResource(id = R.color.dark_green))
        }
        Text(text = "my number?", color = colorResource(id = R.color.dark_green))

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) { // jbhi user textbutton pe click kre to hme countries ka menu dikhna chahiye so isiliye onClick me expanded = true mtlv click krne pe expanded ka value true ho jaye

            Box(modifier = Modifier.width(230.dp)) {
                Text(
                    text = selectedCountry,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.align(
                        Alignment.CenterEnd
                    ),
                    tint = colorResource(id = R.color.light_green)
                )
            }

        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 66.dp),
            thickness = 2.dp,
            color = colorResource(
                id = R.color.light_green
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()) {
            listOf(
                "India",
                "USA",
                "China",
                "Canada"
            ).forEach { country ->// using forEach loop to provide the options when we dropdown our menu
                DropdownMenuItem(text = { Text(text = country) }, onClick = {
                    selectedCountry = country
                    expanded = false
                })
            }
        }

        when (authState) {

            //agr authstate inn 3ino me se kisi bhi state me hai toh ye bloack of code execute hona chahiye
            is AuthState.Ideal, is AuthState.Loading, is AuthState.CodeSent -> {

                if (authState is AuthState.CodeSent) {
                    verificationId = (authState as AuthState.CodeSent).verificationId

                }
                if (verificationId == null) {

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = countryCode ,
                            onValueChange = {countryCode = it} ,
                            modifier = Modifier.width(70.dp) ,
                            singleLine = true ,
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent ,
                                focusedContainerColor = Color.Transparent ,
                                focusedIndicatorColor = colorResource(id = R.color.light_green)

                            )

                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        TextField(
                            value = phoneNumber ,
                            onValueChange = {phoneNumber = it} ,
                            placeholder = {Text("Phone Number")} ,
                            singleLine = true ,
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent ,
                                focusedContainerColor = Color.Transparent ,
                                focusedIndicatorColor = colorResource(id = R.color.light_green)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        if(phoneNumber.isNotEmpty()){

                            val fullPhoneNuber = "$countryCode$phoneNumber"

                            activity?.let{phoneAuthViewModel.sendVerificationCode(fullPhoneNuber , it)} ?: run {Toast.makeText(context, "Unable to access Activity", Toast.LENGTH_SHORT).show()}
                        }else{
                            Toast.makeText(context , "Please enter a valid Phone Number" , Toast.LENGTH_SHORT).show()
                        }
                    }, shape = RoundedCornerShape(6.dp), colors = ButtonDefaults.buttonColors(
                        colorResource(R.color.dark_green)
                    )){

                        Text("Send OTP")
                    }
                    if(authState is AuthState.Loading){
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator()
                    }
                }else{
                    //OTP Input UI
                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        "Enter OTP" ,
                        fontSize = 20.sp ,
                        fontWeight = FontWeight.Bold ,
                        color = colorResource(R.color.dark_green)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = otp ,
                        onValueChange = {otp = it} ,
                        placeholder = {Text("OTP")} ,
                        modifier = Modifier.fillMaxWidth() ,
                        singleLine = true ,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent ,
                            focusedContainerColor = Color.Transparent ,
                            focusedIndicatorColor = colorResource(id = R.color.light_green)
                    )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(onClick = {
                        if(otp.isNotEmpty() && verificationId != null){

                            phoneAuthViewModel.verifyCode(otp , context)
                        }else{
                            Toast.makeText(context , "Please enter a valid OTP" , Toast.LENGTH_SHORT).show()
                        }
                    },shape = RoundedCornerShape(6.dp) , colors = ButtonDefaults.buttonColors(
                        colorResource(R.color.dark_green)
                    ) ){
                        Text("Verify OTP")
                    }

                    if(authState is AuthState.Loading){
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator()
                    }

                }
            }
            is AuthState.Success -> {

                Log.d("PhoneAuth" , "LoginSuccessful")

                phoneAuthViewModel.resetAuthState()

                navController.navigate(Routes.UserProfileSetScreen){
                    popUpTo<Routes.UserRegistrationScreen>{inclusive = true}
                }
            }

            is AuthState.Error -> {
                Toast.makeText(context , (authState as AuthState.Error).message , Toast.LENGTH_SHORT).show()
            }
        }




    }
}

//textstyle use kiyaa hai jisse fontsize badhaa skte h