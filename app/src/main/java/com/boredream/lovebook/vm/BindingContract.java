package com.boredream.lovebook.vm;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseMethod;

import com.boredream.lovebook.common.vmcompose.RefreshUiState;
import com.boredream.lovebook.data.TheDay;
import com.boredream.lovebook.data.TraceLocation;
import com.boredream.lovebook.utils.GlideUtils;
import com.boredream.lovebook.view.RefreshListView;
import com.boredream.lovebook.view.SyncStatusView;
import com.boredream.lovebook.view.TraceMapView;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 用于解决自定义组件的DataBinding问题
 */
public class BindingContract {

    @BindingAdapter("android:src")
    public static void setImageViewGlideUrl(ImageView iv, String newValue) {
        GlideUtils.INSTANCE.load(Glide.with(iv), newValue, iv);
    }

    @BindingAdapter("android:src")
    public static void setPhotoViewImageSource(PhotoView photoView, String imageSource) {
        GlideUtils.INSTANCE.load(Glide.with(photoView), imageSource, photoView);
    }

    @BindingAdapter("myLocation")
    public static void setTraceMapViewMyLocation(TraceMapView traceMapView, TraceLocation location) {
        if (location == null) return;
        traceMapView.setMyLocation(location);
    }

    @BindingAdapter("traceList")
    public static void setTraceMapViewTraceList(TraceMapView traceMapView, ArrayList<TraceLocation> allTracePointList) {
        if (allTracePointList == null) return;
        traceMapView.drawTraceList(allTracePointList);
    }

    @BindingAdapter("historyTraceList")
    public static void setTraceMapViewHistoryTraceList(TraceMapView traceMapView, ArrayList<ArrayList<TraceLocation>> historyTracePointList) {
        if (historyTracePointList == null) return;
        traceMapView.drawMultiFixTraceList(historyTracePointList);
    }

    @BindingAdapter("isFollowing")
    public static void setTraceMapViewFollowingMode(TraceMapView traceMapView, boolean isFollowing) {
        traceMapView.setFollowingMode(isFollowing);
    }

    @BindingAdapter("refreshState")
    public static void setRefreshListViewRefreshState(RefreshListView refreshListView, RefreshUiState refreshUiState) {
        if (refreshUiState != null) refreshListView.updateRefreshState(refreshUiState);
    }

    @BindingAdapter("dataList")
    public static void setRefreshListViewDataList(RefreshListView refreshListView, ArrayList<?> dataList) {
        if (dataList != null) refreshListView.updateDataList(dataList);
    }

    @BindingAdapter("isSyncing")
    public static void setRefreshListViewDataList(SyncStatusView view, boolean isSyncing) {
        if (view != null) view.setRefresh(isSyncing);
    }

    // 用于解决需要转换的数据转换问题
    public static class Convert {

        @InverseMethod("notifyTypeStringToInt")
        public static String notifyTypeIntToString(Integer value) {
            return value != null && value == TheDay.NOTIFY_TYPE_YEAR_COUNT_DOWN ? "按年倒计天数" : "累计天数";
        }

        public static Integer notifyTypeStringToInt(String value) {
            return Objects.equals(value, "累计天数")
                    ? TheDay.NOTIFY_TYPE_TOTAL_COUNT
                    : TheDay.NOTIFY_TYPE_YEAR_COUNT_DOWN;
        }

    }

}
