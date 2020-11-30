package com.example.brdplay

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.brdplay.models.Match
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class NewMatchActivity : AppCompatActivity() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedViewModel =
            ViewModelProvider(this).get(SharedViewModel::class.java)

        setContentView(R.layout.activity_newmatch)
        val activeUsername = intent.getStringExtra("activeUsername")!!

        val gameName: EditText = findViewById(R.id.game_name)
        val isOnline: Switch = findViewById(R.id.online_toggle)
        val matchLocInfo: EditText = findViewById(R.id.game_loc_info)
        val isPrivate: Switch = findViewById(R.id.private_toggle)
        val passwordView: EditText = findViewById(R.id.password)
        val groupName: EditText = findViewById(R.id.group_name)
        val dateBtn: ImageButton = findViewById(R.id.date_picker_btn)
        val dateView: TextView = findViewById(R.id.date_text_view)
        val timeBtn: ImageButton = findViewById(R.id.time_picker_btn)
        val timeView: TextView = findViewById(R.id.time_text_view)
        val createButton: Button = findViewById(R.id.create_match)
        val datePickerFragment = DatePickerFragment(dateView, this)
        val timePickerFragment = TimePickerFragment(timeView, this)

        isOnline.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                matchLocInfo.hint = resources.getString(R.string.match_url)
            } else {
                matchLocInfo.hint = resources.getString(R.string.game_location)
            }
        }

        isPrivate.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                passwordView.visibility = View.VISIBLE
            } else {
                passwordView.visibility = View.GONE
            }
        }

        timeBtn.setOnClickListener {
            timePickerFragment.show(supportFragmentManager, "timePicker")
        }

        dateBtn.setOnClickListener {
            datePickerFragment.show(supportFragmentManager, "datePicker")
        }

        createButton.setOnClickListener {
            val game = gameName.text.toString()
            val owner = activeUsername
            val password = if (isPrivate.isChecked) {
                passwordView.text.toString()
            } else {
                null
            }
            val c = Calendar.getInstance()
            c.set(
                datePickerFragment.year,
                datePickerFragment.month,
                datePickerFragment.day,
                timePickerFragment.hour,
                timePickerFragment.minute
            )
            val dateTime = c.time
            val members = sharedViewModel.getMembers(groupName.text.toString())?.toMutableList()
            val gameUrl = if (isOnline.isChecked) {
                matchLocInfo.text.toString()
            } else {
                null
            }
            val location = if (isOnline.isChecked) {
                null
            } else {
                val results = Geocoder(this).getFromLocationName(matchLocInfo.text.toString(), 1)
                if (results != null) {
                    val location = results.get(0)
                    GeoPoint(location.latitude, location.longitude)
                } else {
                    null
                }
            }
            // Validation
            if (location == null && gameUrl.isNullOrEmpty()) {
                Toast.makeText(this, "Error: There must be at least a location or url", Toast.LENGTH_SHORT).show()
            } else if (members == null) {
                Toast.makeText(this, "Error: Invalid group name", Toast.LENGTH_SHORT).show()
            } else if (dateTime.before(Date())) {
                Toast.makeText(this, "Error: Date set to past", Toast.LENGTH_SHORT).show()
            } else if (game.isEmpty()) {
                Toast.makeText(this, "Error: Invalid game name", Toast.LENGTH_SHORT).show()
            } else {
                if (!members.contains(owner)) {
                    members.add(owner)
                }
                val match = Match(location, members, gameUrl, game, password, owner, dateTime)
                Firebase.firestore
                    .collection("matches")
                    .add(match)
                    .addOnSuccessListener {
                        finish()
                    }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class TimePickerFragment(private val textView: TextView, private val ctx: Context) : DialogFragment(), TimePickerDialog.OnTimeSetListener {
        var minute = 0
        var hour = 0

        init {
            val c = Calendar.getInstance()
            hour = c.get(Calendar.HOUR_OF_DAY)
            minute = c.get(Calendar.MINUTE)

            textView.text = ctx.resources.getString(R.string.hour_format, this.hour, this.minute)
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return TimePickerDialog(activity, this, hour, minute, true)
        }

        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            this.hour = hourOfDay
            this.minute = minute

            textView.text = ctx.resources.getString(R.string.hour_format, this.hour, this.minute)
        }
    }

    class DatePickerFragment(private val textView: TextView, private val ctx: Context) : DialogFragment(), DatePickerDialog.OnDateSetListener {
        var year = 0
        var month = 0
        var day = 0

        init {
            val c = Calendar.getInstance()
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)
            day = c.get(Calendar.DAY_OF_MONTH)

            textView.text = ctx.resources.getString(R.string.date_format, this.day, this.month, this.year)
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return DatePickerDialog(requireContext(), this, year, month, day)
        }

        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            this.year = year
            this.month = month
            this.day = dayOfMonth

            textView.text = ctx.resources.getString(R.string.date_format, this.day, this.month, this.year)
        }
    }

}