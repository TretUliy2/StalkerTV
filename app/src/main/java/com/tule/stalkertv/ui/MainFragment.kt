package com.tule.stalkertv.ui


import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tule.stalkertv.R
import com.tule.stalkertv.adapters.ChannelsAdapter


/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : Fragment() {

    private val channels = Array<String>(100) { "Channel $it" }
    private val TAG: String = "MainFragment"
    private val shPrefPortalUri = "portalUri"
    private val shPrefLogin = "portalLogin"
    private val shPrefPassword = "portalPassword"

    var sharPref: SharedPreferences? = null

    private val channelsUris =
        Array<Uri>(100) { Uri.parse("http://distribution.bbb3d.renderfarming.net/video/mp4/bbb_sunflower_1080p_30fps_normal.mp4") }
    private lateinit var viewAdapter: ChannelsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var playerView: PlayerView
    private lateinit var player: ExoPlayer
    private var mActivity: FragmentActivity? = null

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val channelsList = view.findViewById<RecyclerView>(R.id.channelsList)
        playerView = view.findViewById(R.id.playerView)
        sharPref = activity?.getPreferences(Context.MODE_PRIVATE)
        // Inflate the layout for this fragment
        if (sharPref?.getString(shPrefPortalUri, "") == "") {
            Log.d(TAG, "No portal saved need a dialog window to choose one")
            playerView.visibility = View.GONE
            channelsList.visibility = View.GONE
        } else {
            viewManager = LinearLayoutManager(activity)
            viewAdapter = ChannelsAdapter(channels)
            viewAdapter.setListener(object : ChannelsAdapter.Listener {
                override fun onClick(position: Int) {
                    val dataSource = DefaultDataSourceFactory(activity, Util.getUserAgent(activity, "superbrowser"))
                    val mediaSource =
                        ProgressiveMediaSource.Factory(dataSource).createMediaSource(channelsUris.get(position))
                    player.prepare(mediaSource)
                    player.playWhenReady = true
                    channelsUris.get(position)
                }
            })
            playerView.controllerAutoShow = false

            channelsList.apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
            }
            channelsList.addItemDecoration(DividerItemDecoration(channelsList.context, DividerItemDecoration.VERTICAL))

        }
        mActivity = activity
        return view
    }

    override fun onStart() {
        super.onStart()
        player = ExoPlayerFactory.newSimpleInstance(activity, DefaultTrackSelector())
        playerView.player = player
        player.playWhenReady = true
    }
}
