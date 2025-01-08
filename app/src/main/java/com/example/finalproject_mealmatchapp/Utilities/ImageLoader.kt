package com.example.finalproject_mealmatchapp.Utilities

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.finalproject_mealmatchapp.R
import com.google.firebase.storage.StorageReference
import java.lang.ref.WeakReference

class ImageLoader private constructor(context: Context) {
    private val contextRef = WeakReference(context)

    // Load image from Drawable
    fun load(
        source: Drawable,
        imageView: ImageView,
        placeholder: Int = R.drawable.left_arrow
    ) {
        contextRef.get()?.let { context ->
            Glide
                .with(context)
                .load(source)
                .placeholder(placeholder)
                .centerCrop()
                .into(imageView)
        }
    }

    // Load image from String (URL or Firebase Storage path)
    fun load(
        source: String,
        imageView: ImageView,
        placeholder: Int = R.drawable.left_arrow
    ) {
        contextRef.get()?.let { context ->
            Glide
                .with(context)
                .load(source)
                .placeholder(placeholder)
                .centerCrop()
                .into(imageView)
        }
    }

    // Load image from Uri (e.g., gallery image)
    fun load(
        source: Uri,
        imageView: ImageView,
        placeholder: Int = R.drawable.left_arrow
    ) {
        contextRef.get()?.let { context ->
            Glide
                .with(context)
                .load(source)
                .placeholder(placeholder)
                .centerCrop()
                .into(imageView)
        }
    }

    // Load image from Firebase Storage Reference
    private  fun loadFromFirebase(
        storageReference: StorageReference,
        imageView: ImageView,
        placeholder: Int = R.drawable.left_arrow
    ) {
        contextRef.get()?.let { context ->
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                Glide
                    .with(context)
                    .load(uri)
                    .centerCrop()
                    .into(imageView)
            }.addOnFailureListener {
                // Handle failure
                imageView.setImageResource(R.drawable.left_arrow) // Optional error image
            }
        }
    }

      fun loadFromFirebaseStorage(storageReference: StorageReference, donationIMGIcon: ImageView) {
        loadFromFirebase(storageReference,donationIMGIcon)

    }

    companion object {

        @Volatile
        private var instance: ImageLoader? = null

        fun init(context: Context): ImageLoader {
            return instance ?: synchronized(this) {
                instance ?: ImageLoader(context).also { instance = it }
            }
        }

        fun getInstance(): ImageLoader {
            return instance
                ?: throw IllegalStateException("ImageLoader must be initialized by calling init(context) before use.")
        }
    }
}