package nl.avans.marvelapp.fragments

import android.app.Notification
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import nl.avans.marvelapp.MainActivity
import nl.avans.marvelapp.R
import nl.avans.marvelapp.services.NotificationService

class AccountFragment : Fragment() {

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
            onSelectImage(view)
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
    }

    private fun onSelectImage(view: View) {
        TODO("Not yet implemented")
    }

    private fun onUpdateDetails(view: View) {
        val newName = view.findViewById<EditText>(R.id.etName).text.toString()
        val newEmail = view.findViewById<EditText>(R.id.etEmail).text.toString()

        // Change account details
        MainActivity.account?.name = newName
        MainActivity.account?.email = newEmail

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