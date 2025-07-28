package service;

import java.util.List;
import dao.MemberManagementDao;
import domain.Member;

public class MemberManagementService implements MemberManagementServiceInterface{
private MemberManagementDao memberDao;
	public MemberManagementService()
	{
		this.memberDao=new MemberManagementDao();
	}
	public boolean registerMember(Member member) throws Exception {
	    try {
	        return memberDao.registerNewMember(member);
	    } catch (Exception ex) {
	    	String message = ex.getMessage().toLowerCase();
	        if (message.contains("email already exists")) {
	            throw new IllegalArgumentException("Email already exists. Please use a different email.");
	        } else if (message.contains("mobile number already exists")) {
	            throw new IllegalArgumentException("Mobile number already exists. Please use a different number.");
	        } else if (message.contains("duplicate entry")) {
	            throw new IllegalArgumentException("Email or Mobile number already exists.");
	        } else {
	            throw ex;
	        }
	    }
	}
	public boolean updateMember(int memberId, Member member) throws Exception {
	    if (member.getName() == null &&
	        member.getEmail() == null &&
	        member.getMobile() == null &&
	        member.getAddress() == null) {
	        throw new IllegalArgumentException("At least one field must be provided to update.");
	    }
	    if (member.getEmail() != null && !isValidEmail(member.getEmail())) {
	        throw new IllegalArgumentException("Invalid email format.");
	    }
	    if (member.getMobile() != null && !isValidPhoneNumber(member.getMobile())) {
	        throw new IllegalArgumentException("Mobile number must be exactly 10 digits.");
	    }

	    try {
	        return memberDao.updatermemberDetails(memberId, member);
	    } catch (Exception ex) {
	        String message = ex.getMessage().toLowerCase();
	        if (message.contains("email already exists")) {
	            throw new IllegalArgumentException("Email already exists. Please use a different email.");
	        } else if (message.contains("mobile number already exists")) {
	            throw new IllegalArgumentException("Mobile number already exists. Please use a different number.");
	        } else if (message.contains("duplicate entry")) {
	            throw new IllegalArgumentException("Email or Mobile number already exists.");
	        } else {
	            throw ex;
	        }
	    }
	}
	/*public boolean updateMember(int memberId, Member member) throws Exception {
	    try {
	        return memberDao.updatermemberDetails(memberId, member);
	    } catch (Exception ex) {
	        String message = ex.getMessage().toLowerCase();
	        if (message.contains("email already exists")) {
	            throw new IllegalArgumentException("Email already exists. Please use a different email.");
	        } else if (message.contains("mobile number already exists")) {
	            throw new IllegalArgumentException("Mobile number already exists. Please use a different number.");
	        } else if (message.contains("duplicate entry")) {
	            throw new IllegalArgumentException("Email or Mobile number already exists.");
	        } else {
	            throw ex;
	        }
	    }
	}*/
	public List<Member> getAllMembers() throws Exception {
		return memberDao.viewAllMembers();
        
    }	
	private boolean isValidEmail(String email) {
	    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	    return email != null && email.matches(emailRegex);
	}
	private boolean isValidPhoneNumber(String phone) {
	    return phone != null && phone.matches("\\d{10}");
	}
}