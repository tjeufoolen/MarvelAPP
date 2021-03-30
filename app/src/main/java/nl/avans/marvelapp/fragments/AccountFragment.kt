package nl.avans.marvelapp.fragments

import android.app.Activity.RESULT_OK
import android.app.Notification
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.graphics.decodeBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import nl.avans.marvelapp.MainActivity
import nl.avans.marvelapp.R
import nl.avans.marvelapp.services.NotificationService
import java.io.IOException

class AccountFragment : Fragment() {

    companion object {
        private const val SELECT_IMAGE_REQUEST_CODE: Int = 1
    }

    private lateinit var notificationChannelData: NotificationService.ChannelData
    private var notificationId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize instance variables
        notificationChannelData = NotificationService.ChannelData(
            "account-update-details",
            "Managing account details"
        )

        // Initialize account details
        initializeAccountDetails(view)

        // Handle select image click
        view.findViewById<Button>(R.id.bSelectImage).setOnClickListener {
            onSelectImage()
        }

        // Handle update account details click
        view.findViewById<Button>(R.id.bUpdateDetails).setOnClickListener {
            onUpdateDetails(view)
        }
    }

    private fun initializeAccountDetails(view: View) {
        val name = view.findViewById<EditText>(R.id.etName)
        val email = view.findViewById<EditText>(R.id.etEmail)
        val picture = view.findViewById<ImageView>(R.id.ivProfilePicture)

        name.setText(MainActivity.account?.name)
        email.setText(MainActivity.account?.email)
        picture.setImageBitmap(MainActivity.account?.image)
    }

    private fun onSelectImage() {
        val pickImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickImage, SELECT_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            val uri = data?.data
            try {
                updateImageView(
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            requireActivity().contentResolver,
                            uri!!
                        )
                    )
                )
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun updateImageView(bitmap: Bitmap) {
        view?.findViewById<ImageView>(R.id.ivProfilePicture)?.setImageBitmap(bitmap)
    }

    private fun onUpdateDetails(view: View) {
        val name = view.findViewById<EditText>(R.id.etName).text.toString()
        val email = view.findViewById<EditText>(R.id.etEmail).text.toString()
        val image = view.findViewById<ImageView>(R.id.ivProfilePicture).drawable.toBitmap()

        // Change account details
        MainActivity.account?.name = name
        MainActivity.account?.email = email
        MainActivity.account?.image = image

        // Update views
        val activity: MainActivity = requireContext() as MainActivity
        activity.updateAccountInformation()

        // Send confirmation notification
        sendNotification(view)
    }

    private fun sendNotification(view: View) {
        val notification = NotificationService.build(view.context, NotificationService.NotificationData(
            notificationChannelData,
            "[MarvelAPP] Account",
            "Your account details have been updated!",
            R.drawable.ic_baseline_notifications_24,
            R.drawable.ic_launcher_background
        ))

        if (notificationId == -1) {
            notificationId = NotificationService.send(view.context, notificationChannelData, notification)
        } else {
            NotificationService.update(notificationId, view.context, notificationChannelData, notification)
        }
    }

}