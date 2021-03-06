package cn.video.controller;

import cn.video.annotation.ApiEncryptAnnotation;
import cn.video.base.Result;
import cn.video.controller.vo.ParseVO;
import cn.video.entity.ProposalEntity;
import cn.video.mapper.ProposalMapper;
import cn.video.service.PolymericService;
import cn.video.util.UrlUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/polymeric")
public class PolymericController {

    private final PolymericService polymericService;

    private final ProposalMapper proposalMapper;

    public PolymericController(PolymericService polymericService, ProposalMapper proposalMapper) {
        this.polymericService = polymericService;
        this.proposalMapper = proposalMapper;
    }

    @GetMapping("/parseUrl")
    @ApiEncryptAnnotation
    public Result<ParseVO> parseUrl(@RequestParam String url) {
        return Result.success(this.polymericService.parseUrl(UrlUtil.filterUrl(url)));
    }


    @GetMapping("/downLoadVideo")
    public void downLoadVideo(@RequestParam String url, HttpServletResponse response) {
        this.polymericService.downLoadVideo(url, response);
    }

    @GetMapping("/downLoadImage")
    public void downLoadImage(@RequestParam String url, HttpServletResponse response) {
        this.polymericService.downLoadImage(url, response);
    }

    @GetMapping("/fankui")
    public Result<Void> fankui(@RequestParam String fk) {
//        this.polymericService.fankui(fk);
        ProposalEntity proposalEntity = new ProposalEntity();
        proposalEntity.setProposalData(fk);

        this.proposalMapper.insert(proposalEntity);
        return Result.success(null);
    }

    @GetMapping("/getTitleText")
    @ApiEncryptAnnotation
    public Result<String> getTitleText() {
        String titleText = "支持各平台视频解析!!";

        return Result.success(titleText);
    }

    @PostMapping("/testPost")
    public Object tsetPost(@RequestBody Map<String, Object> data) {
        return JSON.toJSONString(data);
    }


}
