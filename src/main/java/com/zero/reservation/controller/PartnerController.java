package com.zero.reservation.controller;

import com.zero.reservation.model.dto.partner.DeleteStoreDTO;
import com.zero.reservation.model.dto.partner.StoreDTO;
import com.zero.reservation.model.dto.partner.StoreListDTO;
import com.zero.reservation.model.dto.partner.UpdateStoreDTO;
import com.zero.reservation.model.response.BindingResponse;
import com.zero.reservation.model.response.Response;
import com.zero.reservation.service.AccountService;
import com.zero.reservation.service.PartnerService;
import com.zero.reservation.status.Status;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/partner")
@Slf4j

// 파트너
public class PartnerController {

    private final AccountService accountService;

    private final PartnerService partnerService;


    @PostMapping("add")
    public ResponseEntity<?> addStore(@RequestBody @Valid StoreDTO parameter,
                                      BindingResult bindingResult, HttpServletRequest request) {

        log.info("parameter: {}", parameter);
        failedResult(bindingResult);

        String userId = (String) request.getSession().getAttribute("userId");

        Response result = partnerService.addStore(parameter, userId);

        return ResponseEntity.ok(result);
    }

    @GetMapping("store-list")
    public ResponseEntity<?> getStoreList(HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("userId");

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.ok(new Response(Status.NOT_LOGGING_IN));
        }

        List<StoreListDTO> list = partnerService.getStoreList(userId);
        if (list.isEmpty()) {
            return ResponseEntity.ok(new Response(Status.NOT_FOUND_STORE));
        }
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateStore(@RequestBody @Valid UpdateStoreDTO parameter,
                                         BindingResult bindingResult, HttpServletRequest request) {

        failedResult(bindingResult);

        String userId = (String) request.getSession().getAttribute("userId");

        return ResponseEntity.ok(partnerService.updateStore(parameter, userId));
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> removeStore(@RequestBody @Valid DeleteStoreDTO parameter, HttpServletRequest request){
        String userId = (String) request.getSession().getAttribute("userId");

        return ResponseEntity.ok(partnerService.deleteStore(parameter, userId));
    }



    // 매개변수가 null일 경우
    private BindingResponse failedResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasErrors()) {
                FieldError fieldError = bindingResult.getFieldError();
                if (fieldError != null) {
                    return new BindingResponse(false, bindingResult.getFieldError().getDefaultMessage());
                }
            }
        }

        return null;
    }
}