package fr.iut.tapawaru.action;

import java.util.ArrayList;
import fr.iut.tapawaru.map.Cell;
import fr.iut.tapawaru.map.CellPosition;
import fr.iut.tapawaru.map.Map;
import fr.iut.tapawaru.map.TypeGlyph;
import fr.iut.tapawaru.team.Buff;
import fr.iut.tapawaru.team.Character;

/**
 * Attack spells.
 * Library gathering static methods of the different attack spells.
 * Inherited class from Spell, likewise Terra spells.
 * 
 * @authors CEARD, MATHEY, MOUNIER, 
 * @authors PELLOUX-PRAYER, PRADELLE
 */
public class SpellAttack extends Action
{
	/**
	 * chooses a random type among the 4 adjacent glyphs
	 */
	private static TypeGlyph getAttackType(Map map, Cell cellCastingFrom)
	{
		return map.getGlyph(cellCastingFrom.getPosition().generateAdjacentGlyphPosition()[(int)(Math.random() * 4)]).getTypeGlyph();
	}

	
	private static void executeAttack(Map map, ArrayList<CellPosition> posList, TypeGlyph type)
	{
		map.getMapGui().spellAnimation(posList,type);
		switch (type)
		{
			case HOLY:
				for (CellPosition cellPos : posList)
				{
					holyAttack(map, cellPos);
				}
				break;
				
			case FIRE:
				for (CellPosition cellPos : posList)
				{
					fireAttack(map, cellPos);
				}
				break;
				
			case ICE:
				for (CellPosition cellPos : posList)
				{
					iceAttack(map, cellPos);

				}
				break;
				
			case AIR:
				for (CellPosition cellPos : posList)
				{
					airAttack(map, cellPos);

						
				}
				break;
				
			default:
				for (CellPosition cellPos : posList)
				{
					normalAttack(map, cellPos);

				}

		}
		
		
		map.getTeamController().checkWin();

	}


	private static void normalAttack(Map map, CellPosition cellPos) {
		if (map.getCharacter(cellPos.getPositionX(), cellPos.getPositionY()) != null)
		{
			map.getCharacter(cellPos.getPositionX(), cellPos.getPositionY()).inflict(1);
		}
	}


	private static void airAttack(Map map, CellPosition cellPos) {
		if (map.getCharacter(cellPos.getPositionX(), cellPos.getPositionY()) != null)
		{
			Character pers = map.getCharacter(cellPos.getPositionX(), cellPos.getPositionY());
			
			if (pers.getBuff() == Buff.BURNING)
				pers.setBuff(Buff.NORMAL);
			
			pers.inflict(1);
		}
	}


	private static void iceAttack(Map map, CellPosition cellPos) {
		if (map.getCharacter(cellPos.getPositionX(), cellPos.getPositionY()) != null)
		{
			map.getCharacter(cellPos.getPositionX(), cellPos.getPositionY()).setBuff(Buff.FREEZING);
			map.getCharacter(cellPos.getPositionX(), cellPos.getPositionY()).inflict(2);
		}
	}


	private static void fireAttack(Map map, CellPosition cellPos) {
		if (map.getCharacter(cellPos.getPositionX(), cellPos.getPositionY()) != null)
		{
			map.getCharacter(cellPos.getPositionX(), cellPos.getPositionY()).setBuff(Buff.BURNING);
			map.getCharacter(cellPos.getPositionX(), cellPos.getPositionY()).inflict(2);
		}
	}


	private static void holyAttack(Map map, CellPosition cellPos) {
		if (map.getCharacter(cellPos.getPositionX(), cellPos.getPositionY()) != null)
		{
			map.getCharacter(cellPos.getPositionX(), cellPos.getPositionY()).heal(1);
			map.getCharacter(cellPos.getPositionX(), cellPos.getPositionY()).setBuff(Buff.NORMAL);
		}
	}
	
	
	
	
	
	/**
	 * laserBeam
	 * o: caster
	 * x: target
	 * *: impact
	 * 
	 * o****x****
	 * 
	 */
	public static ArrayList<CellPosition> laserBeam(Map map, Character caster, CellPosition target, boolean execute)
	{
		ArrayList<CellPosition> cellList = new ArrayList<CellPosition>();

		laserBeamAttack(map, caster, target, cellList);
		
		return cellList;
	}


	private static void laserBeamAttack(Map map, Character caster, CellPosition target,
			ArrayList<CellPosition> cellList) {
		if (map.getTeamController().isDeductable(1))
		{
		
			int deltaX = target.getPositionX() - caster.getCellTraveled().getPosition().getPositionX();
			int deltaY = target.getPositionY() - caster.getCellTraveled().getPosition().getPositionY();
				
			if (Math.abs(deltaX) > Math.abs(deltaY))
			{
				if (deltaX > 0) //right
				{
					selectRightCell(map, caster, cellList);
				}
				else	//left
				{
					selectLeftCell(caster, cellList);
				}
	
			}
			else
			{
				if (deltaY > 0)	//down
				{
					selectDownCell(map, caster, cellList);
				}
				else	//up
				{
					selectupCell(caster, cellList);
				}
			}
			
			executeAttack(map, cellList, getAttackType(map, caster.getCellTraveled()));
			map.getTeamController().deduct(1);
		}
	}


	private static void selectupCell(Character caster, ArrayList<CellPosition> cellList) {
		for (int x = caster.getCellTraveled().getPosition().getPositionX(),
			 	 y = caster.getCellTraveled().getPosition().getPositionY()-1;
			 y >= 0;
			 y--)
		{
			cellList.add(new CellPosition(x, y));
		}
	}


	private static void selectDownCell(Map map, Character caster, ArrayList<CellPosition> cellList) {
		for (int x = caster.getCellTraveled().getPosition().getPositionX(),
			 	 y = caster.getCellTraveled().getPosition().getPositionY()+1;
			 y < map.getYSize();
			 y++)
		{
			cellList.add(new CellPosition(x, y));
		}
	}


	private static void selectLeftCell(Character caster, ArrayList<CellPosition> cellList) {
		for (int x = caster.getCellTraveled().getPosition().getPositionX()-1,
			 	 y = caster.getCellTraveled().getPosition().getPositionY();
			 x >= 0;
			 x--)
		{
			cellList.add(new CellPosition(x, y));
		}
	}


	private static void selectRightCell(Map map, Character caster, ArrayList<CellPosition> cellList) {
		for (int PosX = caster.getCellTraveled().getPosition().getPositionX()+1,
				 PosY = caster.getCellTraveled().getPosition().getPositionY();
			 PosX < map.getXSize();
			 PosX++)
		{
			cellList.add(new CellPosition(PosX, PosY));
		}
	}
	
	
	
	
	
	/**
	 * aroundCaster
	 * o: caster
	 * *: impact
	 *   ***
	 *   *o*
	 *   ***
	 */
	public static ArrayList<CellPosition> aroundCaster(Map map, Character caster, boolean execute)
	{

		ArrayList<CellPosition> cellList = new ArrayList<CellPosition>();
		
		aroundCasterAttack(map, caster, cellList);


		return cellList;
	}


	private static void aroundCasterAttack(Map map, Character caster, ArrayList<CellPosition> cellList) {
		if (map.getTeamController().isDeductable(1))
		{
			int xCaster = caster.getCellTraveled().getPosition().getPositionX();
			int yCaster = caster.getCellTraveled().getPosition().getPositionY();
			
			selectAroundCell(map, cellList, xCaster, yCaster);
			
			executeAttack(map, cellList, getAttackType(map, caster.getCellTraveled()));
			map.getTeamController().deduct(1);

		}
	}


	private static void selectAroundCell(Map map, ArrayList<CellPosition> cellList, int xCaster, int yCaster) {
		for (int y = yCaster - 1 ; y <= yCaster + 1 ; y++)
		{
			for (int x = xCaster - 1 ; x <= xCaster + 1 ; x++)
			{
				if ((x != xCaster || y != yCaster) &&
					x >= 0 && y >= 0 &&
					x < map.getXSize() && y < map.getYSize())
					cellList.add(new CellPosition(x, y));
			}
		}
	}

	
	
	
	
	/**
	 * flowerBomb
	 * o: caster
	 * X: target/impact
	 * *: impact
	 *      *
	 * o   *X*
	 *      *
	 */
	public static ArrayList<CellPosition> flowerBomb(Map map, Character caster, CellPosition target, boolean execute)
	{
		
		ArrayList<CellPosition> cellList = new ArrayList<CellPosition>();
		
		if (map.getTeamController().isDeductable(1))
		{
			flowerBonbSelectCell(map, target, cellList);
			
			executeAttack(map, cellList, getAttackType(map, caster.getCellTraveled()));
			map.getTeamController().deduct(1);

		}


		return cellList;
	}


	private static void flowerBonbSelectCell(Map map, CellPosition target, ArrayList<CellPosition> cellList) {
		int x, y,
			xTarget = target.getPositionX(),
			yTarget = target.getPositionY();
		
		x = xTarget; y = yTarget+1;
		if (map.checkPosition(map, x, y))
			cellList.add(new CellPosition(x, y));
		
		x = xTarget; y = yTarget-1;
		if (map.checkPosition(map, x, y))
			cellList.add(new CellPosition(x, y));
		
		x = xTarget+1; y = yTarget;
		if (map.checkPosition(map, x, y))
			cellList.add(new CellPosition(x, y));
		
		x = xTarget-1; y = yTarget;
		if (map.checkPosition(map, x, y))
			cellList.add(new CellPosition(x, y));
		
		x = xTarget; y = yTarget;
		if (map.checkPosition(map, x, y))
			cellList.add(new CellPosition(x, y));
	}

}
