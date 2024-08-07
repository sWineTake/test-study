package com.tester.part02.application.port.out;
import com.tester.part02.domain.Board;
import java.util.Optional;

public interface LoadBoardPort {
    Optional<Board> findBoardById(Long boardId);
}
