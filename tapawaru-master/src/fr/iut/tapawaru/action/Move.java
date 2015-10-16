package fr.iut.tapawaru.action;

import java.util.ArrayList;

import fr.iut.tapawaru.map.CellPosition;
import fr.iut.tapawaru.map.Map;
import fr.iut.tapawaru.team.Buff;
import fr.iut.tapawaru.team.Character;

/**
 * Movements.
 * One movement is an action, just as a spell.
 * In fact, there's only one possible movement: towards an adjacent box.
 * This class only exists to add other possible movements if necessary.
 * 
 * @authors CEARD, MATHEY, MOUNIER, 
 * @authors PELLOUX-PRAYER, PRADELLE
 */
public class Move extends Action
{
	/**
	 * Move the given Character to the targeted Place.
	 * This deduct Action Point!
	 * @param map Concerned Map.
	 * @param pers Character we want to move.
	 * @param target Destination of the Character.
	 */
	public static void simpleMove(Map map, Character pers, CellPosition target)
	{
		int deltaX = Math.abs(pers.getCellTraveled().getPosition().getPositionX() - target.getPositionX());
		int deltaY = Math.abs(pers.getCellTraveled().getPosition().getPositionY() - target.getPositionY());
		
		if (getAccessiblePos(map, pers).contains(target))
		{
			if (map.getTeamController().isDeductable(deltaX + deltaY))
			{
				map.moveCharacter(pers, target);
				
				map.getTeamController().deduct(deltaX + deltaY);
			}
		}
	}
	
	/**
	 * @param map Concerned Map.
	 * @param pers Character concerned.
	 * @return Accessible cell depending of action point.
	 */
	public static ArrayList<CellPosition> getAccessiblePos(Map map, Character pers)
	{
		ArrayList<CellPosition> cellList = new ArrayList<CellPosition>();

		if (pers.getBuff() == Buff.FREEZING)	//can't move
			return cellList;
		
		int persPosY = pers.getCellTraveled().getPosition().getPositionY();
		int persPosX = pers.getCellTraveled().getPosition().getPositionX();
		int currentActionPoint = map.getTeamController().getPlayingTeam().getActionPointsLeft();
		
		for (int y = persPosY - currentActionPoint ;y < persPosY + currentActionPoint + 1 ;y++)
		 {
				for (int x = persPosX - currentActionPoint ;x < persPosX + currentActionPoint + 1 ;x++)
				 {
					 if (x >= 0 && y >= 0 &&
						 x < map.getXSize() && y < map.getYSize())
					 {
						 if (map.getCell(new CellPosition(x, y)).getCharacter() == null)
						 {
							int deltaX = Math.abs(persPosX - x);
							int deltaY = Math.abs(persPosY - y);
							if (deltaX + deltaY <= currentActionPoint)
							{
								cellList.add(new CellPosition(x, y));
							}
						 }
					 }
				 }
			 }
		return cellList;
	}
}
