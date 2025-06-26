package com.cmdlee.quizsushi.admin.repository;

import com.cmdlee.quizsushi.report.model.Report;
import com.cmdlee.quizsushi.admin.dto.response.StatRawProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @EntityGraph(attributePaths = {"member"})
    Page<Report> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query(value = """
                SELECT '신고' AS label,
                       DATE_TRUNC(:type, r.created_at) AS time,
                       COUNT(*) AS count
                FROM report r
                WHERE r.created_at >= :start AND r.created_at < :end
                GROUP BY time
                ORDER BY time DESC
            """, nativeQuery = true)
    List<StatRawProjection> findReportStats(@Param("type") String type,
                                            @Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end
                                            );

}
