package com.example.samvad.presentation.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.samvad.R
import com.example.samvad.presentation.viewmodel.BaseViewModel

@Composable

fun ChatDesign(
    chatDesignModel : ChatDesignModel ,
    onClick:() -> Unit ,
    baseViewModel: BaseViewModel
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        val profileImage = chatDesignModel?.profileImage
        val bitmap = remember {
            profileImage?.let{baseViewModel.base64ToBitmap(it)}
        }
        Image(
            painter = if(bitmap != null) {
                rememberImagePainter(bitmap) }
            else{
                painterResource(id = R.drawable.profile_placeholder)
    }, contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .background(color = Color.Gray)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))

        Column() {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = chatDesignModel.name?: "Unknown" , fontSize = 18.sp , fontWeight = FontWeight.Bold)
                Text(text = chatDesignModel.time?: "--:--" , color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(chatDesignModel.message?:"messaage" , color = Color.Gray , fontSize = 14.sp , fontWeight = FontWeight.SemiBold)
        }


    }

}
