package lib.time;

public interface ReservationObserver {
	//Zaman aşımı olduğunda odanın adını ve tablodaki satır numarasını verir 
	void onReservationTimeout(String roomName, int rowIndex);
}
