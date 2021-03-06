package Tileset.Behaviour;

import java.util.List;

import Interface.Stages.Selections.BehaviourSelection;
import State.Coord;
import State.State;

// Move directly in the best route towards player
public class MovePathToPoint extends MoveBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3159919302846591258L;
	private boolean focus;
	 
	public MovePathToPoint(boolean focus) {
		super.setSelection(BehaviourSelection.PATH2POINT);
		this.focus = focus;
	}
	
	@Override
	public Coord nextStep(State s, Coord currentPos) {
		List<Coord> path = findRoute(s, currentPos, s.findPlayer());
		if (path == null || path.size() == 0 || s.isBlocked(path.get(0))) {
			// non path behaviour
			if (focus) {
				return currentPos;
			} else {
				return new MoveRandom().nextStep(s, currentPos);
			}
		} else {
			return path.get(0);
		}		
	}
	
}
