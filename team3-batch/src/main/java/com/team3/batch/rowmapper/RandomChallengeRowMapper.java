package com.team3.batch.rowmapper;

import com.team3.batch.vo.RandomChallengeVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class RandomChallengeRowMapper implements RowMapper<RandomChallengeVO> {
    @Override
    public RandomChallengeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new RandomChallengeVO(
                rs.getLong("member_id"),
                rs.getLong("challenge_id")
        );
    }
}
