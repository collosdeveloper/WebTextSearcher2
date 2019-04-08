package com.webtextsearcher.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.webtextsearcher.R;
import com.webtextsearcher.base.adapters.UrlsListAdapter;
import com.webtextsearcher.data.RequiredSearchData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = MainActivity.class.getSimpleName();
	
	@BindView(R.id.til_start_url)
	TextInputLayout tilStartUrl;
	@BindView(R.id.edt_start_url)
	TextInputEditText edtStartUrl;
	@BindView(R.id.til_scan_thread_count)
	TextInputLayout tilScanThreadCount;
	@BindView(R.id.edt_scan_thread_count)
	TextInputEditText edtScanThreadCount;
	@BindView(R.id.til_searched_text)
	TextInputLayout tilSearchedText;
	@BindView(R.id.edt_searched_text)
	TextInputEditText edtSearchedText;
	@BindView(R.id.til_count_of_scanned_url)
	TextInputLayout tilCountOfScannedUrl;
	@BindView(R.id.edt_count_of_scanned_url)
	TextInputEditText edtCountOfScannedUrl;
	@BindView(R.id.btn_pause)
	Button btnPause;
	@BindView(R.id.btn_start)
	Button btnStart;
	@BindView(R.id.btn_stop)
	Button btnStop;
	@BindView(R.id.recycle_view_of_scanned_url)
	RecyclerView rvOfScannedUrl;
	@BindView(R.id.progress_bar)
	ProgressBar progressBar;
	
	private WebSearchViewModel viewModel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		iniUI();
		initData();
	}
	
	private void iniUI() {
		setContentView(R.layout.activity_main);
		
		ButterKnife.bind(this);
		
		btnStart.setOnClickListener(v -> viewModel.startSearch(
				new RequiredSearchData(edtStartUrl.getText().toString(),
						edtScanThreadCount.getText().toString(),
						edtSearchedText.getText().toString(),
						edtCountOfScannedUrl.getText().toString()), false));
		btnStop.setOnClickListener(v -> viewModel.stopSearch());
		btnPause.setOnClickListener(v -> viewModel.pauseSearch());

		/* */ String url = "https://www.bbc.com";
		edtStartUrl.setText(url);
		
		rvOfScannedUrl.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		rvOfScannedUrl.setHasFixedSize(true);
		rvOfScannedUrl.setAdapter(new UrlsListAdapter());
	}
	
	private void initData() {
		viewModel = ViewModelProviders.of(this).get(WebSearchViewModel.class);
		viewModel.getWebSearchStatus().observe(this, this::handleWebSearchStatus);
		viewModel.getLinks().observe(this, this::fillRecycleView);
	}
	
	private void fillRecycleView(List<String> links) {
		progressBar.setVisibility(View.GONE);
		((UrlsListAdapter) rvOfScannedUrl.getAdapter()).setUrls(links);
	}
	
	private void handleWebSearchStatus(WebSearchStatus webSearchStatus) {
		switch (webSearchStatus) {
			case SHOW_LOADER:
				hideLoader(false);
				break;
			case HIDE_LOADER:
				hideLoader(true);
				break;
			case ENABLE_VIEW:
				enableControlViews(true);
				break;
			case DISABLE_VIEW:
				enableControlViews(false);
				break;
			case URL_FORMAT_ERROR:
				tilStartUrl.setError(getString(R.string.wrong_url_error));
				break;
			case THREAD_COUNT_ERROR:
				tilScanThreadCount.setError(getString(R.string.wrong_thread_count_error));
				break;
			case SEARCHED_TEXT_ERROR:
				tilSearchedText.setError(getString(R.string.searched_text_error));
				break;
			case MAX_SCAN_URL_ERROR:
				tilCountOfScannedUrl.setError(getString(R.string.scanned_url_error));
				break;
			case REMOVE_ALL_ERROR:
				removeAllErrorFromTilLayouts();
				break;
			case PAUSE_ENABLE:
				btnPause.setText(getString(R.string.pause));
				break;
			case PAUSE_DISABLE:
				btnPause.setText(getString(R.string.pause_off));
				break;
		}
	}
	
	private void enableControlViews(boolean status) {
		edtStartUrl.setEnabled(status);
		edtScanThreadCount.setEnabled(status);
		edtSearchedText.setEnabled(status);
		edtCountOfScannedUrl.setEnabled(status);
		btnStart.setEnabled(status);
		btnStop.setEnabled(status);
	}
	
	private void removeAllErrorFromTilLayouts() {
		tilStartUrl.setError(null);
		tilScanThreadCount.setError(null);
		tilSearchedText.setError(null);
		tilCountOfScannedUrl.setError(null);
	}
	
	private void hideLoader(boolean hide) {
		if (hide) {
			progressBar.setVisibility(View.GONE);
			btnStart.setEnabled(true);
			btnPause.setEnabled(false);
			btnStop.setEnabled(false);
		} else {
			progressBar.setVisibility(View.VISIBLE);
			btnStart.setEnabled(false);
			btnPause.setEnabled(true);
			btnStop.setEnabled(true);
		}
	}
}