package uz.ilhomjon.mlkittest1

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.datatransport.BuildConfig
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_photo)
            .setOnClickListener {
                var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent,0)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val bitmap = data?.extras?.get("data") as Bitmap
        val imageView = findViewById<ImageView>(R.id.my_image_view)
        imageView.setImageBitmap(bitmap)
        analyzeImage(bitmap)
    }

        private fun analyzeImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)

        // ML Kit bilan tasvir yorliqlash
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

            val tv = findViewById<TextView>(R.id.tv_info)
        labeler.process(image)
            .addOnSuccessListener { labels ->
                var resultText = "Aniqlangan obyektlar:\n"
                for (label in labels) {
                    val text = label.text
                    val confidence = label.confidence
                    resultText += "$text (Ishonchlilik: ${confidence * 100}%)\n"
                }
                tv.text = resultText
                Toast.makeText(this, resultText, Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                tv.text = "Xatolik: ${e.message}"
                Toast.makeText(this, "Xatolik: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


}