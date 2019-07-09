package ronybrosh.rocketlauncher.presentation.features.rocketlist.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ronybrosh.rocketlauncher.presentation.R
import ronybrosh.rocketlauncher.presentation.features.common.model.PresentableRocket
import ronybrosh.rocketlauncher.presentation.features.common.view.ViewModelFragment
import ronybrosh.rocketlauncher.presentation.utils.observe
import timber.log.Timber

class RocketListFragment : ViewModelFragment<RocketListViewModel>(
    RocketListViewModel::class.java
), View.OnClickListener {

    private val adapter: RocketListAdapter = RocketListAdapter(
        this
    )
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var isFilterOn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.let { fragmentActivity ->
            if (fragmentActivity is AppCompatActivity) {
                fragmentActivity.apply {
                    supportActionBar?.title = getString(R.string.app_name)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    menuInflater.inflate(R.menu.menu_with_filter_button, menu)
                    refreshFilterIcon(menu.findItem(R.id.filter))
                }
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter -> {
                isFilterOn = !isFilterOn
                refreshFilterIcon(item)
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
                showRocketDetails(tag)
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
        }
    }

    private fun refreshFilterIcon(menuItem: MenuItem) {
        menuItem.setIcon(
            when (isFilterOn) {
                true -> {
                    R.drawable.ic_filter_list_on
                }
                false -> {
                    R.drawable.ic_filter_list_off
                }
            }
        )
    }

    private fun showRocketDetails(presentableRocket: PresentableRocket) {

    }
}