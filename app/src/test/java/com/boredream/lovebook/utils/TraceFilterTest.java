package com.boredream.lovebook.utils;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.boredream.lovebook.data.TraceLocation;
import com.boredream.lovebook.data.TraceRecord;
import com.google.gson.Gson;

import junit.framework.TestCase;

import java.io.File;
import java.util.ArrayList;

public class TraceFilterTest extends TestCase {

    public void getTraceInfo() {
        File dir = new File("src/test/source");
        File file = new File(dir, "test_trace.json");
        String json = FileIOUtils.readFile2String(file);
        TraceRecord record = new Gson().fromJson(json, TraceRecord.class);
        ArrayList<TraceLocation> traceList = record.getTraceList();
        for (int i = 1; i < traceList.size(); i++) {
            float distance = AMapUtils.calculateLineDistance(
                    new LatLng(traceList.get(i - 1).getLatitude(), traceList.get(i - 1).getLongitude()),
                    new LatLng(traceList.get(i).getLatitude(), traceList.get(i).getLongitude()));
            if(distance > 10) {
                System.out.println(i + " -> distance=" + distance + ", " + traceList.get(i) + ", time=" + traceList.get(i).getTime());
            }
        }
    }

    public void testFilterPos() {
        // 1279 -> distance=11.263475, 2022-11-27 13:51:15  31.218509108894036,121.34865032130207, time=1669528275904
        // 1314 -> distance=33.162727, 2022-11-27 13:52:25  31.219405726411782,121.34866050254615, time=1669528345776
        // 1805 -> distance=12.325929, 2022-11-27 14:08:49  31.221116763603547,121.34565372806745, time=1669529329768
        // 1817 -> distance=535.6648, 2022-11-27 14:09:21  31.218749,121.340748, time=1669529361913
        // 1858 -> distance=489.26813, 2022-11-27 14:11:41  31.221442,121.344817, time=1669529501882
        // 1867 -> distance=489.26813, 2022-11-27 14:12:19  31.218749,121.340748, time=1669529539857
        // 1881 -> distance=532.97626, 2022-11-27 14:13:16  31.221136998240162,121.34560771819545, time=1669529596881
        // 1883 -> distance=12.583787, 2022-11-27 14:13:18  31.22115278699941,121.3457387582326, time=1669529598778
        // 1945 -> distance=269.49402, 2022-11-27 14:15:40  31.221203,121.348427, time=1669529740918
        // 1952 -> distance=238.14105, 2022-11-27 14:16:09  31.221152846768334,121.34592333468251, time=1669529769777
        File dir = new File("src/test/source");
        File file = new File(dir, "test_trace.json");
        String json = FileIOUtils.readFile2String(file);
        TraceRecord record = new Gson().fromJson(json, TraceRecord.class);
        ArrayList<TraceLocation> traceList = record.getTraceList();
        TraceFilter filter = new TraceFilter();
        ArrayList<TraceLocation> newTraceList = new ArrayList<>();
        for (int i = 1800; i < 1900; i++) {
            boolean isValidate = filter.filterPos(traceList.get(i));
            if(!isValidate) {
//                System.out.println(i + " -> " + traceList.get(i) + ", time=" + traceList.get(i).getTime());
            } else {
                newTraceList.add(traceList.get(i));
            }
        }

        for (int i = 1; i < newTraceList.size(); i++) {
            float distance = AMapUtils.calculateLineDistance(
                    new LatLng(newTraceList.get(i - 1).getLatitude(), newTraceList.get(i - 1).getLongitude()),
                    new LatLng(newTraceList.get(i).getLatitude(), newTraceList.get(i).getLongitude()));
            System.out.println(i + " -> distance=" + distance + ", " + newTraceList.get(i) + ", time=" + newTraceList.get(i).getTime());
        }
    }

}