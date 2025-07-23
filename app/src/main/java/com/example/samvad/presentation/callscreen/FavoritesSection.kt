package com.example.samvad.presentation.callscreen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samvad.R

@Composable
@Preview(showSystemUi = true)
fun FavoritesSection() {

    val sampleFavorites = listOf(
        FavoriteContact(image = R.drawable.p11 , name = "Josh"),
        FavoriteContact(image = R.drawable.p5 , name = "Nick"),
        FavoriteContact(image = R.drawable.p9 , name = "Cook"),
        FavoriteContact(image = R.drawable.p2 , name = "Smith"),
        FavoriteContact(image = R.drawable.p10 , name = "Boult"),
        FavoriteContact(image = R.drawable.p7 , name = "Maxwell"),
        FavoriteContact(image = R.drawable.p7 , name = "Maxwell"),
        FavoriteContact(image = R.drawable.p7 , name = "Maxwell"),
        FavoriteContact(image = R.drawable.p7 , name = "Maxwell")


    )
    Column(modifier = Modifier.padding(start = 16.dp , bottom = 8.dp)) {
        Text(
            text = "Favorites",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())){ //horizontalScroll(rememberScrollState) --> ye horizontal scroll krne ke liye h ...
            sampleFavorites.forEach{
                FavoritesItem(it)
            }

        }
    }
}

data class FavoriteContact(val image : Int , val name : String)
