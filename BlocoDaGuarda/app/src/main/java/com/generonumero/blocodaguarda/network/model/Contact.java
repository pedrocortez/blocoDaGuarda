package com.generonumero.blocodaguarda.network.model;


public class Contact {

    private Integer id;
    private String name;
    private String phone;

    public Contact() {
    }

    public Contact(Integer id) {
        this.id = id;
    }

    public Contact(Integer id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhoneFormated() {
        if(phone != null) {
            return phone.replaceAll("[^\\d.]", "");
        }
        return phone;
    }

    public Integer getId() {
        return id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public boolean isValid() {
        if(this.getName() == null || this.getName().equals("")) {
            return false;
        }
        if(this.getPhone() == null || this.getPhone().equals("")) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (name != null ? !name.equals(contact.name) : contact.name != null) return false;
        return phone != null ? phone.equals(contact.phone) : contact.phone == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }


}
