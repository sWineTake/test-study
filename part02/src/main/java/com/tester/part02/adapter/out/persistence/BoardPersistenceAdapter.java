package com.tester.part02.adapter.out.persistence;

import com.tester.part02.adapter.out.persistence.repository.BoardRepository;
import com.tester.part02.application.port.out.LoadBoardPort;
import com.tester.part02.domain.Board;
import org.springframework.stereotype.Component;
import com.tester.part02.adapter.out.persistence.entity.BoardJpaEntity;
import java.util.Optional;

@Component
public class BoardPersistenceAdapter implements LoadBoardPort {
    private final BoardRepository boardRepository;

    public BoardPersistenceAdapter(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Optional<Board> findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
            .map(BoardJpaEntity::toDomain);
    }
}
