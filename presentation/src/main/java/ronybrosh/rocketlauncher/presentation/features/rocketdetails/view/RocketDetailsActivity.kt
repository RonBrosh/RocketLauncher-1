package ronybrosh.rocketlauncher.presentation.features.rocketdetails.view

import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_rocket_details.*
import ronybrosh.rocketlauncher.presentation.R
import ronybrosh.rocketlauncher.presentation.features.common.model.PresentableRocket
import ronybrosh.rocketlauncher.presentation.features.common.view.ViewModelActivity
import ronybrosh.rocketlauncher.presentation.features.common.view.stickyheaders.StickyHeaderItemDecoration
import ronybrosh.rocketlauncher.presentation.utils.SnackbarUtil
import ronybrosh.rocketlauncher.presentation.utils.observe

class RocketDetailsActivity : ViewModelActivity<RocketDetailsViewModel>(
    RocketDetailsViewModel::class.java
) {
    private lateinit var presentableRocket: PresentableRocket

    private val adapter: RocketLaunchesAdapter = RocketLaunchesAdapter()

    companion object {
        const val INTENT_EXTRA_PRESENTABLE_ROCKET = "INTENT_EXTRA_PRESENTABLE_ROCKET"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presentableRocket = intent.getSerializableExtra(INTENT_EXTRA_PRESENTABLE_ROCKET) as PresentableRocket
        setContentView(R.layout.activity_rocket_details)
        setSupportActionBar(toolbar)
        initToolbar()
        initSelectedRocketDetails()
        initRecyclerView()
        initSwipeToRefresh()
        setupViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (it.itemId == android.R.id.home) {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewModel() {
        observe(viewModel.getLoading()) { isLoading ->
            swipeRefreshLayout.isRefreshing = isLoading
        }

        observe(viewModel.getError()) { errorMessageResourceId ->
            SnackbarUtil.showWithRetryAction(getString(errorMessageResourceId), rocketDetailsRootView) {
                viewModel.refresh()
            }
        }

        observe(viewModel.getResult()) { result ->
            adapter.setData(result)
        }
        viewModel.setRocketId(presentableRocket.id, presentableRocket.description)
    }

    private fun initToolbar() {
        supportActionBar?.title = presentableRocket.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent))
    }

    private fun initSelectedRocketDetails() {
        ViewCompat.setTransitionName(rocketImage, presentableRocket.id)
        Picasso.with(this).load(presentableRocket.imageUrl).placeholder(getDrawable(R.drawable.ic_image_place_holder))
            .into(rocketImage)
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.addItemDecoration(
            StickyHeaderItemDecoration(
                resources.getDrawable(R.drawable.list_divider, null), adapter
            )
        )
        recyclerView.adapter = adapter
    }

    private fun initSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }
}