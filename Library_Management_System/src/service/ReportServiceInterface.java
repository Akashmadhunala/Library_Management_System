package service;
import domain.IssueRecord;
import domain.Member;
import exceptions.ManagementException;

import java.util.List;
import java.util.Map;

public interface ReportServiceInterface {
	
    public List<IssueRecord> getOverdueBooks() ;
    public Map<String, Long> getBooksCountPerCategory() throws ManagementException ;
    public List<Member> getMembersWithActiveIssuedBooks();
    
}

