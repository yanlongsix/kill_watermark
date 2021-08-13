package cn.video.parse.handler;

import cn.video.parse.Parse;
import cn.video.util.HttpRequestHeaderUtil;
import cn.video.util.HttpUtil;
import cn.video.vo.ParseVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Map;

public class DouYinParseHandler implements Parse {

    private static final String ItemUrl = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=";

    @Override
    public ParseVO parseUrl(String url) throws IOException {
        // 获取 重定向地址
        Map<String, String> headMap = HttpRequestHeaderUtil.getHeadMap();
        String locationUrl = HttpUtil.getLocationUrl(url, headMap);


        String itemId = getItemId(locationUrl);
        String responseBody = HttpUtil.getBody(ItemUrl + itemId, headMap);

        JSONObject responseBodyJsonObj = JSON.parseObject(responseBody);

        JSONObject itemListJsonObj = responseBodyJsonObj.getJSONArray("item_list").getJSONObject(0);

        JSONObject videoObject = itemListJsonObj.getJSONObject("video");

        String videoUrl = videoObject.getJSONObject("play_addr").getJSONArray("url_list").getString(0).replace("playwm", "play");

        // 获取视频的重定向地址

        String locationVideoUrl = HttpUtil.getLocationUrl(videoUrl, headMap);
        return new ParseVO(videoUrl, locationVideoUrl);
    }


    public static String getItemId(String url) {
        int start = url.indexOf("/video/") + "/video/".length();
        int end = url.lastIndexOf("/");

        return url.substring(start, end);
    }
}
