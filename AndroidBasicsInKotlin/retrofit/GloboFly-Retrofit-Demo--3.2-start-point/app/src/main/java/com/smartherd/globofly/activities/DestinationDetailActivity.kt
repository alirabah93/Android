package com.smartherd.globofly.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.smartherd.globofly.R
import com.smartherd.globofly.helpers.SampleData
import com.smartherd.globofly.models.Destination
import com.smartherd.globofly.services.DestinationService
import com.smartherd.globofly.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_destiny_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DestinationDetailActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_destiny_detail)

		setSupportActionBar(detail_toolbar)
		// Show the Up button in the action bar.
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		val bundle: Bundle? = intent.extras

		if (bundle?.containsKey(ARG_ITEM_ID)!!) {

			val id = intent.getIntExtra(ARG_ITEM_ID, 0)

			loadDetails(id)

			initUpdateButton(id)

			initDeleteButton(id)
		}
	}

	private fun loadDetails(id: Int) {

		// To be replaced by retrofit code
//		val destination = SampleData.getDestinationById(id)
//
//		destination?.let {
//			et_city.setText(destination.city)
//			et_description.setText(destination.description)
//			et_country.setText(destination.country)
//
//			collapsing_toolbar.title = destination.city
//		}
		val destinationService : DestinationService = ServiceBuilder.buildService(DestinationService::class.java)
		val requestCall : Call<Destination> = destinationService.getDestination(id)

		requestCall.enqueue(object: retrofit2.Callback<Destination> {

			override fun onResponse(call: Call<Destination>, response: Response<Destination>) {

				if (response.isSuccessful) {
					val destination : Destination? = response.body()
					destination?.let {
						et_city.setText(destination.city)
						et_description.setText(destination.description)
						et_country.setText(destination.country)

						collapsing_toolbar.title = destination.city
					}
				} else {
					Toast.makeText(this@DestinationDetailActivity, "Failed to retrieve details", Toast.LENGTH_LONG).show()
				}
			}

			override fun onFailure(call: Call<Destination>, t: Throwable) {
				Toast.makeText(this@DestinationDetailActivity, "Error Occurred" + t.toString(), Toast.LENGTH_LONG).show()
			}
		})
	}

	private fun initUpdateButton(id: Int) {

		btn_update.setOnClickListener {

			val city = et_city.text.toString()
			val description = et_description.text.toString()
			val country = et_country.text.toString()

            // To be replaced by retrofit code
//            val destination = Destination()
//            destination.id = id
//            destination.city = city
//            destination.description = description
//            destination.country = country
//
//            SampleData.updateDestination(destination);
//            finish() // Move back to DestinationListActivity

			val destinationService : DestinationService = ServiceBuilder.buildService(DestinationService::class.java)
			val requestCall : Call<Destination> = destinationService.updateDestination(id, city, description, country)

			requestCall.enqueue(object: Callback<Destination> {

				override fun onResponse(call: Call<Destination>, response: Response<Destination>) {
					if (response.isSuccessful) {
						finish() // Move back to DestinationListActivity
						var updatedDestination : Destination? = response.body() // Use it or ignore it
						Toast.makeText(this@DestinationDetailActivity, "Item Updated Successfully", Toast.LENGTH_SHORT).show()
					} else {
						Toast.makeText(this@DestinationDetailActivity, "Failed to update item", Toast.LENGTH_SHORT).show()
					}
				}

				override fun onFailure(call: Call<Destination>, t: Throwable) {
					Toast.makeText(this@DestinationDetailActivity, "Failed to update item", Toast.LENGTH_SHORT).show()
				}
			})
		}
	}

	private fun initDeleteButton(id: Int) {

		btn_delete.setOnClickListener {

            // To be replaced by retrofit code
//            SampleData.deleteDestination(id)
//            finish() // Move back to DestinationListActivity

			val destinationService : DestinationService = ServiceBuilder.buildService(DestinationService::class.java)
			val requestCall : Call<Unit> = destinationService.deleteDestination(id)

			requestCall.enqueue(object: Callback<Unit> {

				override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
					if (response.isSuccessful) {
						finish() // Move back to DestinationListActivity
						var deletedDestination : Unit? = response.body() // Use it or ignore it
						Toast.makeText(this@DestinationDetailActivity, "Item Deleted Successfully", Toast.LENGTH_SHORT).show()
					} else {
						Toast.makeText(this@DestinationDetailActivity, "Failed to delete item", Toast.LENGTH_SHORT).show()
					}
				}

				override fun onFailure(call: Call<Unit>, t: Throwable) {
					Toast.makeText(this@DestinationDetailActivity, "Failed to delete item", Toast.LENGTH_SHORT).show()
				}
			})
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		val id = item.itemId
		if (id == android.R.id.home) {
			navigateUpTo(Intent(this, DestinationListActivity::class.java))
			return true
		}
		return super.onOptionsItemSelected(item)
	}

	companion object {

		const val ARG_ITEM_ID = "item_id"
	}
}
