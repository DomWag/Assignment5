package com.acertainbank.client.workloads;

public class WorkerRunResult {

	private int numTransfers;
	private long elapsedTimeInNanoSecs;
	
	public WorkerRunResult (int numTransfers, long elapsedTimeInNanoSecs){
	
	this.numTransfers = numTransfers;
	this.elapsedTimeInNanoSecs = elapsedTimeInNanoSecs;
	}

	public int getNumTransfers() {
		return numTransfers;
	}

	public void setNumTransfers(int numTransfers) {
		this.numTransfers = numTransfers;
	}

	public long getElapsedTimeInNanoSecs() {
		return elapsedTimeInNanoSecs;
	}

	public void setElapsedTimeInNanoSecs(int elapsedTimeInNanoSecs) {
		this.elapsedTimeInNanoSecs = elapsedTimeInNanoSecs;
	}
	
	
}
