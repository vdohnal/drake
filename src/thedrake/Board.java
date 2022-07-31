package thedrake;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board implements JSONSerializable {
	private final BoardTile[][] tiles;
	private final int dimension;

	// Konstruktor. Vytvoří čtvercovou hrací desku zadaného rozměru,
	// kde všechny dlaždice jsou prázdné, tedy BoardTile.EMPTY
	public Board(int dimension) {
		this.dimension = dimension;
		tiles = new BoardTile[dimension][dimension];

		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++)
				tiles[i][j] = BoardTile.EMPTY;
		}
	}

	private Board(BoardTile[][] tiles) {
		this.tiles = tiles;
		dimension  = tiles.length;
	}

	// Rozměr hrací desky
	public int dimension() {
		return dimension;
	}

	// Vrací dlaždici na zvolené pozici.
	public BoardTile at(TilePos pos) {
		return tiles[pos.i()][pos.j()];
	}

	// Vytváří novou hrací desku s novými dlaždicemi. Všechny ostatní dlaždice zůstávají stejné
	public Board withTiles(TileAt ...ats) {
		BoardTile[][] newTiles = new BoardTile[dimension][dimension];

		for (int i = 0; i < dimension; i++)
			newTiles[i] = Arrays.copyOf(tiles[i], dimension);

		for (TileAt at : ats)
			newTiles[at.pos.i()][at.pos.j()] = at.tile;

		return new Board(newTiles);
	}

	// Vytvoří instanci PositionFactory pro výrobu pozic na tomto hracím plánu
	public PositionFactory positionFactory() {
		return new PositionFactory(dimension);
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.print("{\"dimension\":" + dimension + ",\"tiles\":[");

		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (i != 0 || j != 0)
					writer.print(",");
				tiles[j][i].toJSON(writer);
			}
		}

		writer.print("]}");
	}

	public static class TileAt {
		public final BoardPos pos;
		public final BoardTile tile;
		
		public TileAt(BoardPos pos, BoardTile tile) {
			this.pos = pos;
			this.tile = tile;
		}
	}
}

