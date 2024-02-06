package com.example.projetmap

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetmap.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123 // Définissez un code de demande d'autorisation unique.
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            // Vérifiez d'abord si l'autorisation a déjà été accordée
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Vous avez déjà l'autorisation, vous pouvez accéder à la localisation ici.
                findNavController().navigate(R.id.action_FirstFragment_to_MapsFragment)
            } else {
                // Demandez l'autorisation à l'utilisateur
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        // Demandez l'autorisation à l'utilisateur
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // L'autorisation a été accordée, vous pouvez accéder à la localisation ici.
                    findNavController().navigate(R.id.action_FirstFragment_to_MapsFragment)
                } else {
                    // L'autorisation a été refusée, vous devrez informer l'utilisateur des fonctionnalités limitées.
                    showPermissionDeniedDialog()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showPermissionDeniedDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Autorisation de localisation refusée")
        dialogBuilder.setMessage("Pour utiliser pleinement cette application, n'hésitez pas à aller autoriser l'accès à la localisation dans les réglages.")
        dialogBuilder.setPositiveButton("OK") { _: DialogInterface, _: Int ->
            findNavController().navigate(R.id.action_FirstFragment_to_MapsFragment)
        }
        dialogBuilder.setCancelable(false)
        val dialog = dialogBuilder.create()
        dialog.show()
    }
}
