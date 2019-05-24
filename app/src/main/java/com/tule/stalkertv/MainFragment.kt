package com.tule.stalkertv


import android.net.Uri
import android.nfc.tech.TagTechnology
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.android.synthetic.main.fragment_main.*
import kotlin.math.acos


/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : Fragment() {

    private val channels = Array<String>(100) { "Channel $it" }
    private val TAG: String = "MainFragment"
    private val channelsUris = Array<Uri> (100) { Uri.parse("http://82.193.96.217:12999/udp/239.203.0.5:1234") }
    private lateinit var viewAdapter : RecyclerView.Adapter<*>
    private lateinit var viewManager : RecyclerView.LayoutManager
    private lateinit var playerView : PlayerView
    private lateinit var player : ExoPlayer
    private var mActivity: FragmentActivity? = null
    companion object {
        fun newInstance() : MainFragment {
            return MainFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mActivity = activity
        viewManager  = LinearLayoutManager(activity)
        viewAdapter = ChannelsAdapter(channels)
        val view =  inflater.inflate(R.layout.fragment_main, container, false)
        val channelsList = view.findViewById<RecyclerView>(R.id.channelsList)
        playerView = view.findViewById(R.id.playerView)
        playerView.controllerAutoShow = false

        channelsList.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        channelsList.addItemDecoration(DividerItemDecoration(channelsList.context, DividerItemDecoration.VERTICAL))
        return view
    }

    override fun onStart() {
        super.onStart()
        player = ExoPlayerFactory.newSimpleInstance(activity, DefaultTrackSelector())
        playerView.player = player
        player.playWhenReady = true
    }

    class ChannelsAdapter (private val channels : Array<String>)
        :RecyclerView.Adapter<ChannelsAdapter.ChannelsViewHolder>() {
        override fun getItemCount(): Int {
            return channels.size
        }

        override fun onBindViewHolder(holder: ChannelsViewHolder, position: Int) {
            val textView = holder.linearView.findViewById<TextView>(R.id.channel_text_view)
            holder.linearView.setOnClickListener({ Log.d("Hello", "Hello $position") })
            textView.text = channels[position]
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelsViewHolder {
            val linearLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.channel_text_view, parent, false) as LinearLayout

            return ChannelsViewHolder(linearLayout)
        }

        class ChannelsViewHolder ( val linearView: LinearLayout) : RecyclerView.ViewHolder(linearView)
    }

}
