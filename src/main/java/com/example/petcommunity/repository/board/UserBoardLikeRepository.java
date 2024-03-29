package com.example.petcommunity.repository.board;

import com.example.petcommunity.entity.board.BoardEntity;
import com.example.petcommunity.entity.board.UserBoardLike;
import com.example.petcommunity.entity.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBoardLikeRepository extends JpaRepository<UserBoardLike, Long> {
    Optional<UserBoardLike> findByUserAndBoard(MemberEntity user, BoardEntity board);
}
