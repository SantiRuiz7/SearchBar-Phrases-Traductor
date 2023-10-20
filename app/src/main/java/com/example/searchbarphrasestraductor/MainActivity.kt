package com.example.searchbarphrasestraductor


import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toAndroidRect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.searchbarphrasestraductor.ui.theme.SearchBarPhrasesTraductorTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            println("Clicked on Pip Action")
        }
    }

    private val isPipSupported by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            packageManager.hasSystemFeature(
                PackageManager.FEATURE_PICTURE_IN_PICTURE
            )
        } else {
            false
        }
    }

    private var videoViewBounds = Rect()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchBarPhrasesTraductorTheme {
                val ctx = LocalContext.current

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var query by remember { mutableStateOf("") }
                    var active by remember { mutableStateOf(false) }

                    SearchBar(
                        query = query,
                        onQueryChange = { query = it },
                        onSearch = {
                            Toast.makeText(ctx, query, Toast.LENGTH_SHORT).show()
                            active = false
                        },
                        active = active,
                        onActiveChange = { active = it},
                        placeholder = { Text(text = "Search")},
                        leadingIcon = { IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null)
                        }},
                        trailingIcon = { IconButton(onClick = { /*TODO Agregar video en un toolbar */ }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null)
                        }}
                    ) {
                        if (query.isNotEmpty()){
                        val filteredWords = palabras.filter { it.contains(query, true) }
                        filteredWords.forEach{
                            palabras ->
                            Text("$palabras")
                        }
                            showAndroidView(query)
                    }
                }
            }
        }
    }
}


@Composable
private fun showAndroidView(query:String){

    if ( palabras.contains(query)) {
        AndroidView(
            factory = {
                VideoView( it, null).apply {
                    setVideoURI(Uri.parse("android.resource://$packageName/${R.raw.hola}"))
                    start()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    videoViewBounds = it
                        .boundsInWindow()
                        .toAndroidRect()
                }
        )
    }
}

    private fun updatedPipParams(): PictureInPictureParams? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureParams.Builder()
                .setSourceRectHint(videoViewBounds)
                .setAspectRatio(Rational(16,9))
                .setActions(
                    listOf(
                        RemoteAction(
                            Icon.createWithResource(
                                applicationContext,
                                R.drawable.baseline_arrow_forward_ios_24
                            ),
                            "Video",
                            "Descripcion",
                            PendingIntent.getBroadcast(
                                applicationContext,
                                0,
                                Intent(applicationContext, MyReceiver::class.java),
                                PendingIntent.FLAG_IMMUTABLE
                            )
                        )
                    )
                )
                .build()
        } else null
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (!isPipSupported) {
            return
        }
        updatedPipParams()?.let { params ->

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                enterPictureInPictureMode(params)
            }
        }
    }


private val palabras = listOf(
    "Hola",
    "Como",
    "estás",
    "Bien",
    "Adiós",
    "Por favor",
    "Gracias",
    "Amor",
    "Familia",
    "Amigo",
    "Comida",
    "Casa",
    "Trabajo",
    "Escuela",
    "Libro",
    "Música",
    "Tiempo",
    "Feliz",
    "Triste",
    "Bueno",
    "Malo",
    "Verde",
    "Rojo",
    "Azul",
    "Amarillo",
    "Gato",
    "Perro",
    "Sol",
    "Luna",
    "Mar",
    "Montaña",
    "Río",
    "Árbol",
    "Flor",
    "Ciudad",
    "Playa",
    "Montaña",
    "Viaje",
    "Deporte",
    "Cine",
    "Música",
    "Arte",
    "Dinero",
    "Felicidad",
    "Amor",
    "Paz",
    "Salud"
)
}
