package com.example.searchbarphrasestraductor


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.searchbarphrasestraductor.ui.theme.SearchBarPhrasesTraductorTheme
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchBarPhrasesTraductorTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var query by remember { mutableStateOf("") }
                    var active by remember { mutableStateOf(false) }
                    var isTrailingIconClicked by remember { mutableStateOf(false) }
                    var isCardVisible by remember { mutableStateOf(false) }

                    val words = query.split(" ")

                    val videoMapping = mapOf(
                        "hola" to "hola.m4v",
                        "gracias" to "gracias.m4v"
                        )

                    val assetManager = applicationContext.assets


                    SearchBar(
                        query = query,
                        onQueryChange = { query = it },
                        onSearch = {
                            active = false
                        },
                        active = active,
                        onActiveChange = { active = it },

                        placeholder = { Text(text = "Search") },
                        leadingIcon = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = null
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                isTrailingIconClicked = true
                            },
                                enabled = true) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null
                                )
                            }
                        }
                    ) {
                        if (isCardVisible) {

                            LazyColumn {
                                items(words) { word ->

                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth(0.2f)
                                            .padding(10.dp)
                                    ) {
                                        Text(text = word)

                                        AndroidView(
                                            factory = {
                                                VideoView(it, null).apply {
                                                    val videoFileName = videoMapping[word] ?: ""
                                                    if (videoFileName.isNotEmpty()) {
                                                        try {
                                                            val inputStream = assetManager.open(videoFileName)
                                                            setVideoURI(Uri.parse(inputStream.toString()))
                                                            start()
                                                        } catch (e: IOException) {
                                                            // Si no encuentra el archivo o ocurre un error al abrirlo.
                                                            Log.e("VideoApp", "Error al abrir el archivo: $videoFileName", e)
                                                        }
                                                    }
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(5.dp)
                                        )
                                    }
                                }
                            }
                            LaunchedEffect(isTrailingIconClicked) {
                                  if (isTrailingIconClicked) {
                                    isCardVisible = true
                                    isTrailingIconClicked = false
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
