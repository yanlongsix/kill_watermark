package cn.video.service.impl;

import cn.hutool.http.HttpRequest;
import cn.video.controller.vo.ParseVO;
import cn.video.exceptions.BasicException;
import cn.video.parse.Parse;
import cn.video.parse.ParseContext;
import cn.video.service.PolymericService;
import cn.video.util.HttpRequestHeaderUtil;
import cn.video.util.UrlUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Service
public class PolymericServiceImpl implements PolymericService {

    @Override
    public ParseVO parseUrl(String url) {
        String domain = UrlUtil.getDomain(url);
        if (!StringUtils.hasLength(domain)) {
            throw new BasicException(500, "解析失败");
        }

        domain = domain.replace("https://", "").replace("http://", "");
        Parse parser = ParseContext.getParser(domain);

        if (null == parser) throw new BasicException(500, "解析失败");
        try {
            return parser.parseUrl(url);
        } catch (IOException e) {
            throw new BasicException(500, "解析失败");
        }
    }

    @Override
    public void downLoadVideo(String url, HttpServletResponse response) {
        // 1.下载网络文件
        download(url, response);
        response.setHeader("Content-Type", "video/mp4");
    }

    @Override
    public void downLoadImage(String url, HttpServletResponse response) {
        download(url, response);
    }

    private void download(String url, HttpServletResponse response) {
        int byteRead;
        try {
            String agent = HttpRequestHeaderUtil.getAgent();
            InputStream inputStream = HttpRequest.get(url).header("user-agent", agent).execute().bodyStream();
            //3.写入文件
            ServletOutputStream outputStream = response.getOutputStream();

            byte[] buffer = new byte[2048];
            while ((byteRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, byteRead);
            }
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BasicException(500, "导出失败");
        }
    }

}
