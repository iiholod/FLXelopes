package org.eltech.ddm.miningcore.algorithms;

import java.io.Serializable;

public interface BlockExecuteListener extends Serializable {
	 void doEvent(MiningBlock block, EventType e);
//		public void setAssignedStep(MiningBlock assignedStep);
//		public void delAssignedStep();
}
