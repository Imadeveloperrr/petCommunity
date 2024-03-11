package com.example.petcommunity.service.board;

import com.example.petcommunity.dto.board.BoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {

    void saveBoard(BoardDTO dailyBoardDTO);

    Page<BoardDTO> getAllBoards(Pageable pageable);
    BoardDTO getBoardDetail(int boardNo);
}
