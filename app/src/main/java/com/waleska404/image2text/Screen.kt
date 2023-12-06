package com.waleska404.image2text

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Composable
fun Screen(
    context: Context
) {

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    
    var myVisualText by remember {
        mutableStateOf("")
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        ) {
            Text(text = "pick a photo")
        }
        Text(text = myVisualText)
    }
    
    if (selectedImageUri != null) {
        myVisualText = tryTextRecog(selectedImageUri!!, context)
    }

}

fun tryTextRecog(uri: Uri, context: Context): String {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val image = InputImage.fromFilePath(context, uri)
    var myText = ""
    val result = recognizer.process(image)
        .addOnSuccessListener { visionText ->
            myText = visionText.text
            Log.i("MYTAG", "SUCCESS RECOGNIZING TEXT: $myText")
        }
        .addOnFailureListener { e ->
            Log.e("ERROR", "error recognizing text: $e")
        }

    return myText
}