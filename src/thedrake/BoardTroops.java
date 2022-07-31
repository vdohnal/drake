package thedrake;

// import java.util.Collections;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Consumer;

public class BoardTroops implements JSONSerializable {
	private final PlayingSide 			   playingSide;
	private final Map<BoardPos, TroopTile> troopMap;
	private final TilePos 				   leaderPosition;
	private final int 					   guards;

	public BoardTroops(PlayingSide playingSide) {
		this(playingSide, new HashMap<>(), TilePos.OFF_BOARD, 0);
	}
	
	public BoardTroops(
			PlayingSide playingSide,
			Map<BoardPos, TroopTile> troopMap,
			TilePos leaderPosition, 
			int guards) {
		this.playingSide 	= playingSide;
		this.troopMap 		= troopMap;
		this.leaderPosition = leaderPosition;
		this.guards 		= guards;
	}

	public Optional<TroopTile> at(TilePos pos) {
		TroopTile tile = troopMap.get(pos);
		return tile == null ? Optional.empty() : Optional.of(tile);
	}
	
	public PlayingSide playingSide() {
		return playingSide;
	}
	
	public TilePos leaderPosition() {
		return leaderPosition;
	}

	public int guards() {
		return guards;
	}
	
	public boolean isLeaderPlaced() {
		return leaderPosition != TilePos.OFF_BOARD;
	}
	
	public boolean isPlacingGuards() {
		return isLeaderPlaced() && troopMap.size() < 3;
	}	
	
	public Set<BoardPos> troopPositions() {
		return troopMap.keySet();
	}

	public BoardTroops placeTroop(Troop troop, BoardPos target) {
		if (troopMap.containsKey(target))
			throw new IllegalArgumentException();

		TilePos 				 newLeaderPosition = leaderPosition;
		int 					 newGuards 		   = guards;
		Map<BoardPos, TroopTile> newMap			   = new HashMap<>(troopMap);

		newMap.put(target, new TroopTile(troop, playingSide, TroopFace.AVERS));
		if (!isLeaderPlaced())
			newLeaderPosition = target;
		else if (isPlacingGuards())
			newGuards++;

		return new BoardTroops(playingSide, newMap, newLeaderPosition, newGuards);
	}
	
	public BoardTroops troopStep(BoardPos origin, BoardPos target) {
		if (!isLeaderPlaced() || isPlacingGuards())
			throw new IllegalStateException(
					"Cannot move troops before the leader and guards are placed.");
		if (!troopMap.containsKey(origin) || troopMap.containsKey(target))
			throw new IllegalArgumentException();

		TilePos 				 newLeaderPosition = leaderPosition;
		Map<BoardPos, TroopTile> newMap	   		   = new HashMap<>(troopMap);
		TroopTile 				 tile 			   = newMap.remove(origin);

		newMap.put(target, tile.flipped());
		if (origin.equals(leaderPosition))
			newLeaderPosition = target;

		return new BoardTroops(playingSide, newMap, newLeaderPosition, guards);
	}
	
	public BoardTroops troopFlip(BoardPos origin) {
		if (!isLeaderPlaced())
			throw new IllegalStateException(
					"Cannot move troops before the leader is placed.");
		if (isPlacingGuards())
			throw new IllegalStateException(
					"Cannot move troops before guards are placed.");
		if (at(origin).isEmpty())
			throw new IllegalArgumentException();
		
		Map<BoardPos, TroopTile> newMap = new HashMap<>(troopMap);
		TroopTile 				 tile   = newMap.remove(origin);
		newMap.put(origin, tile.flipped());

		return new BoardTroops(playingSide(), newMap, leaderPosition, guards);
	}
	
	public BoardTroops removeTroop(BoardPos target) {
		if (!isLeaderPlaced() || isPlacingGuards())
			throw new IllegalStateException(
					"Cannot remove troops before the leader and guards are placed.");
		if (!troopMap.containsKey(target))
			throw new IllegalArgumentException();

		TilePos 				 newLeaderPosition = leaderPosition;
		Map<BoardPos, TroopTile> newMap 		   = new HashMap<>(troopMap);

		newMap.remove(target);
		if (target.equals(leaderPosition))
			newLeaderPosition = TilePos.OFF_BOARD;

		return new BoardTroops(playingSide, newMap, newLeaderPosition, guards);
	}

	@Override
	public void toJSON(PrintWriter writer) {
		Consumer<BoardPos> entryPrinter = pos -> {
			pos.toJSON(writer);
			writer.print(":");
			troopMap.get(pos).toJSON(writer);
		};

		writer.print("{\"side\":");
		playingSide.toJSON(writer);
		writer.print(",\"leaderPosition\":");
		leaderPosition.toJSON(writer);
		writer.print(",\"guards\":" + guards + ",\"troopMap\":{");

		NavigableSet<BoardPos> positions = new TreeSet<>(
				(p1, p2) -> (100 * p1.i() + p1.j()) - (100 * p2.i() + p2.j())
		);
		positions.addAll(troopMap.keySet());
		Iterator<BoardPos> iterator = positions.iterator();
		if (iterator.hasNext()) {
			BoardPos first = iterator.next();
			entryPrinter.accept(first);
		}
		iterator.forEachRemaining(pos -> {
			writer.print(",");
			entryPrinter.accept(pos);
		});

		writer.print("}}");
	}
}
