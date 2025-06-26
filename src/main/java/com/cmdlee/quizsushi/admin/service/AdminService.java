package com.cmdlee.quizsushi.admin.service;

import com.cmdlee.quizsushi.admin.domain.model.Report;
import com.cmdlee.quizsushi.admin.domain.model.enums.StatType;
import com.cmdlee.quizsushi.admin.dto.AdminDashboardStats;
import com.cmdlee.quizsushi.admin.dto.response.StatRawProjection;
import com.cmdlee.quizsushi.admin.dto.response.StatRawResponse;
import com.cmdlee.quizsushi.admin.repository.ReportRepository;
import com.cmdlee.quizsushi.member.repository.MemberRepository;
import com.cmdlee.quizsushi.quiz.repository.QuizRepository;
import com.cmdlee.quizsushi.quiz.repository.QuizSolveLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;
    private final QuizRepository quizRepository;
    private final QuizSolveLogRepository quizSolveLogRepository;
    private final ReportRepository reportRepository;

    public AdminDashboardStats getTodayStats() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        long joined = memberRepository.countByDate(start, end);
        long created = quizRepository.countByDate(start, end);
        long solved = quizSolveLogRepository.countByDate(start, end);

        return new AdminDashboardStats(joined, created, solved);
    }

    public List<StatRawResponse> getAllStats(StatType type, int limit) {
        String truncType = type.getSqlTruncUnit();

        List<StatRawProjection> joinStats = memberRepository.findJoinStats(truncType, limit);
        List<StatRawProjection> quizStats = quizRepository.findCreatedStats(truncType, limit);
        List<StatRawProjection> solveStats = quizSolveLogRepository.findSolvedStats(truncType, limit);


        return Stream.of(joinStats, quizStats, solveStats)
                .flatMap(Collection::stream)
                .map(p -> new StatRawResponse(p.getLabel(), p.getTime(), p.getCount()))
                .toList();
    }

    public Page<Report> getReports(Pageable pageable) {
        return reportRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

}
