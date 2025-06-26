package com.cmdlee.quizsushi.quiz.repository;

import com.cmdlee.quizsushi.admin.dto.response.StatRawProjection;
import com.cmdlee.quizsushi.member.domain.model.QuizsushiMember;
import com.cmdlee.quizsushi.quiz.domain.model.MemberQuizSolveLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberQuizSolveLogRepository extends JpaRepository<MemberQuizSolveLog, Long> {
    List<MemberQuizSolveLog> findByMember(QuizsushiMember member);

    Page<MemberQuizSolveLog> findByMember(QuizsushiMember member, Pageable pageable);

    @Query(value = """
            SELECT '풀이' AS label,
                   DATE_TRUNC(:type, s.submitted_at) AS time,
                   COUNT(*) AS count
            FROM member_quiz_solve_log s
            GROUP BY time
            ORDER BY time DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<StatRawProjection> findSolvedStats(@Param("type") String type, @Param("limit") int limit);

    @Query("""
                SELECT COUNT(s)
                FROM MemberQuizSolveLog s
                WHERE s.submittedAt >= :start AND s.submittedAt < :end
            """)
    long countByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
