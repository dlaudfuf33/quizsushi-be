package com.cmdlee.quizsushi.member.service;

import com.cmdlee.quizsushi.admin.domain.model.Report;
import com.cmdlee.quizsushi.admin.repository.ReportRepository;
import com.cmdlee.quizsushi.global.exception.ErrorCode;
import com.cmdlee.quizsushi.global.exception.GlobalException;
import com.cmdlee.quizsushi.member.domain.model.QuizsushiMember;
import com.cmdlee.quizsushi.member.dto.request.ReportRequest;
import com.cmdlee.quizsushi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void report(ReportRequest request, Long memberId) {
        QuizsushiMember member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND));

        Report report = Report.builder()
                .reason(request.)
                .build();


        reportRepository.save(report);
    }
}
