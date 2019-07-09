package ronybrosh.rocketlauncher.presentation.features.rocketlist.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_rocket_list.*
import kotlinx.android.synthetic.main.viewholder_rocket_item.view.*
import ronybrosh.rocketlauncher.presentation.R
import ronybrosh.rocketlauncher.presentation.features.common.model.PresentableRocket
import ronybrosh.rocketlauncher.presentation.features.common.view.ViewModelActivity
import ronybrosh.rocketlauncher.presentation.features.rocketdetails.view.RocketDetailsActivity
import ronybrosh.rocketlauncher.presentation.utils.observe
import timber.log.Timber

class RocketListActivity : ViewModelActivity<RocketListViewModel>(
    RocketListViewModel::class.java
), View.OnClickListener {

    private val adapter: RocketListAdapter = RocketListAdapter(this)
    private var filterMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rocket_list)
        setSupportActionBar(toolbar)
        setupViews()
        setupViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_with_filter_button, menu)
        filterMenuItem = menu?.findItem(R.id.filter)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (it.itemId == R.id.filter) {
                it.isChecked = !it.isChecked
                refreshFilterIcon()
                viewModel.toggleFilter()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {
        if (view == null)
            return
        view.tag.let { tag ->
            if (tag is PresentableRocket) {
                showRocketDetails(view.rocketImage, tag)
            }
        }
    }

    private fun setupViewModel() {
        observe(viewModel.getLoading()) { isLoading ->
            swipeRefreshLayout.isRefreshing = isLoading
        }

        observe(viewModel.getError()) { errorMessage ->
            Timber.e(errorMessage)
        }

        observe(viewModel.getResult()) { result ->
            adapter.setData(result)
            recyclerView.scheduleLayoutAnimation()
        }
    }

    private fun setupViews() {
        swipeRefreshLayout.setOnRefreshListener {
            filterMenuItem?.isChecked = false
            refreshFilterIcon()
            viewModel.refresh()
        }

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    private fun showRocketDetails(view: View, presentableRocket: PresentableRocket) {
        val activityOptionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            Pair.create(
                findViewById<View>(android.R.id.navigationBarBackground),
                Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME
            ),
            Pair.create(
                findViewById<View>(android.R.id.statusBarBackground),
                Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME
            ),
            Pair.create(
                toolbar,
                ViewCompat.getTransitionName(toolbar) ?: ""
            ),
            Pair.create(
                view,
                ViewCompat.getTransitionName(view) ?: ""
            )
        )

        val intent = Intent(this, RocketDetailsActivity::class.java)
        intent.putExtra(RocketDetailsActivity.INTENT_EXTRA_PRESENTABLE_ROCKET, presentableRocket)
        startActivity(intent, activityOptionsCompat.toBundle())
    }

    private fun refreshFilterIcon() {
        filterMenuItem?.let {
            it.setIcon(
                when (it.isChecked) {
                    true -> {
                        R.drawable.ic_filter_list_on
                    }
                    false -> {
                        R.drawable.ic_filter_list_off
                    }
                }
            )
        }
    }
}