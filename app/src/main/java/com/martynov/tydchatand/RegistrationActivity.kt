package com.martynov.tydchatand

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.martynov.tydchatand.data.AttachmentModel
import com.martynov.tydchatand.data.AttachmentType
import com.martynov.tydchatand.databinding.ActivityRegistrationBinding
import com.martynov.tydchatand.repository.NotifictionHelper
import kotlinx.coroutines.launch
import splitties.toast.toast

private lateinit var binding: ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    private var attachmentModel: AttachmentModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        supportActionBar!!.setSubtitle(R.string.registration)
        binding.attachPhotoImgSetting.setOnClickListener {
            dispatchTakePictureIntent()
        }
        binding.btnRegister.setOnClickListener {
            lifecycleScope.launch {
                val login = binding.edtRegistrationLogin.text?.toString().orEmpty()
                val password = binding.edtRegistrationPassword.text?.toString().orEmpty()
                val twoPassword = binding.edtRegistrationRepeatPassword.text?.toString().orEmpty()
                when {
                    !isValidUsername(login) -> {
                        binding.tilRegistrationLogin.error = getString(R.string.username_is_incorrect)
                    }
                    !isValidPassword(password) -> {
                        binding.tilRegistrationPassword.error = getString(R.string.password_is_incorrect)
                    }
                    !(password == twoPassword) -> {
                        binding.tilRegistrationRepeatPassword.error =
                                getString(R.string.password_mismatch)
                    }
                    login == "" -> {
                        toast(getString(R.string.enter_login))
                    }
                    password == "" -> {
                        toast(getString(R.string.enter_password))
                    }
                    twoPassword == "" -> {
                        toast(getString(R.string.enter_your_password_a_second_time))
                    }
                    else -> {
                        dialog = ProgressDialog(this@RegistrationActivity).apply {
                            setMessage(getString(R.string.please_wait))
                            setTitle(getString(R.string.loading_data))
                            show()
                            setCancelable(false)
                        }
                        try {
                            val token = App.repository.register(login, password, attachmentModel)
                            if (token.isSuccessful) {
                                setUserAuth(requireNotNull(token.body()?.token))
                                navigateToFeed()
                            }
                            dialog?.dismiss()

                        } catch (e: Exception) {
                            toast(getString(R.string.falien_connect))
                            dialog?.dismiss()

                        }
                    }
                }
            }
        }

    }

    private fun dispatchTakePictureIntent() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Загрузить фото из")
                .setPositiveButton(
                        getString(R.string.camera),
                        DialogInterface.OnClickListener { dialog, id -> //camera intent
                            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                                takePictureIntent.resolveActivity(packageManager)?.also {
                                    startActivityForResult(
                                            takePictureIntent,
                                            Companion.REQUEST_CAMERA
                                    )
                                }
                            }
                        })
                .setNegativeButton(
                        getString(R.string.gallerea),
                        DialogInterface.OnClickListener { dialog, id ->
                            val permissionStatus = ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            )

                            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                                val loadIntent = Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                )

                                startActivityForResult(loadIntent, REQUEST_GALLERY)
                            } else {
                                ActivityCompat.requestPermissions(
                                        this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                        REQUEST_GALLERY
                                )
                            }
                        })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    companion object {
        const val REQUEST_CAMERA = 0
        const val REQUEST_GALLERY = 1

    }

    private fun imageUploaded() {
        transparetAllIcons()
        binding.attachPhotoDoneImgSetting.visibility = View.VISIBLE
    }

    private fun transparetAllIcons() {
        binding.attachPhotoImgSetting.setImageResource(R.drawable.ic_image_inactive)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK || data == null) {
            return
        }
        val imageBitmap: Bitmap? =
                when (requestCode) {
                    REQUEST_CAMERA -> {
                        data.extras?.get("data") as Bitmap?
                    }
                    REQUEST_GALLERY -> {
                        data.data?.let { uri ->
                            contentResolver.openInputStream(uri).use {
                                BitmapFactory.decodeStream(it)
                            }
                        }
                    }
                    else -> {
                        null
                    }
                }
        imageBitmap?.let {
            lifecycleScope.launch {
                dialog = ProgressDialog(this@RegistrationActivity).apply {
                    setMessage(this@RegistrationActivity.getString(R.string.please_wait))
                    setTitle("Отправка изображения")
                    setCancelable(false)
                    setProgressBarIndeterminate(true)
                    show()
                }
                val imageUploadResult = App.repository.uploadUser(it)
                NotifictionHelper.mediaUploaded(AttachmentType.IMAGE, this@RegistrationActivity)
                dialog?.dismiss()
                if (imageUploadResult.isSuccessful) {
                    imageUploaded()
                    attachmentModel = imageUploadResult.body()
                } else {
                    toast("Не удачная загрузка фото")
                }
            }
        }

    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_GALLERY -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val loadIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )

                    startActivityForResult(loadIntent, REQUEST_GALLERY)
                } else {
                    toast(getString(R.string.permission_not_given))
                }
            }

        }

    }

    private fun setUserAuth(token: String) =
            getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
                    .edit()
                    .putString(AUTHENTICATED_SHARED_KEY, token)
                    .apply()

    private fun navigateToFeed() {
        val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}