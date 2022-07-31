package thedrake;

import java.util.ArrayList;
import java.util.List;

public class SlideAction extends TroopAction {

    public SlideAction(Offset2D offset) {
        super(offset);
    }

    public SlideAction(int offsetX, int offsetY) {
        super(offsetX, offsetY);
    }

    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> result = new ArrayList<>();
        TilePos target = origin;

        while ((target = target.stepByPlayingSide(offset(), side)) != TilePos.OFF_BOARD) {
            if (state.canCapture(origin, target)) {
                result.add(new StepAndCapture(origin, (BoardPos) target));
                break;
            }

            if (!state.canStep(origin, target))
                break;

            result.add(new StepOnly(origin, (BoardPos) target));
        }

        return result;
    }
}
