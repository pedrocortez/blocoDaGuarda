package com.generonumero.blocodaguarda.network.view.impl;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.generonumero.blocodaguarda.BDGApplication;
import com.generonumero.blocodaguarda.R;
import com.generonumero.blocodaguarda.menu.view.impl.MainActivity;
import com.generonumero.blocodaguarda.network.adapter.PickContacts;
import com.generonumero.blocodaguarda.network.model.Contact;
import com.generonumero.blocodaguarda.network.presenter.NetworkPresenter;
import com.generonumero.blocodaguarda.network.view.NetworkView;
import com.generonumero.blocodaguarda.view.ContactView;

import java.util.ArrayList;
import java.util.List;

public class NetworkFragment extends Fragment implements NetworkView, PickContacts {

    private static final String PERMISSION = Manifest.permission.READ_CONTACTS;

    private ContactView contact1;
    private ContactView contact2;
    private ContactView contact3;

    private NetworkPresenter networkPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkPresenter = BDGApplication.getInstance().getNetworkPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.network_frag, null);
        contact1 = (ContactView) view.findViewById(R.id.contact1);
        contact2 = (ContactView) view.findViewById(R.id.contact2);
        contact3 = (ContactView) view.findViewById(R.id.contact3);
        view.findViewById(R.id.bdg_network_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Contact> contacts = new ArrayList<>(3);
                contacts.add(contact1.getContact());
                contacts.add(contact2.getContact());
                contacts.add(contact3.getContact());

                networkPresenter.saveAllContacts(contacts);

                MainActivity activity = (MainActivity) getActivity();
                activity.goToHome();
            }
        });
        networkPresenter.loadViews();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        networkPresenter.onReceiveDataFromContact(this, requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        networkPresenter.onRequestPermissionsResult(getActivity(), requestCode, grantResults, PERMISSION);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contact1 = null;
        contact2 = null;
        contact3 = null;
        networkPresenter = null;
    }

    @Override
    public void OnLoadViews(List<Contact> contacts) {
        loadContact(contact1, contacts.get(0), 0);
        loadContact(contact2, contacts.get(1), 1);
        loadContact(contact3, contacts.get(2), 2);
    }

    private void loadContact(ContactView view, Contact contact, final int position) {
        view.setContact(contact);

        view.setName(contact.getName());
        view.setPhone(contact.getPhone());
        view.setContactLabel(position);

        view.setAddressBookClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickPickContacts(position);
            }
        });
    }

    @Override
    public void showAlertPermissionDenied() {
        Toast.makeText(getContext(), "Precisamos dessa permissão para lhe ajudar a pegar os contatos da agenda.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showAlertPermissionDisable() {
        Toast.makeText(getContext(), "Pedido de permissão para acessar a agenda negado. Vá em configurações e habilite-o para poder usar a agenda e pegar os seus contatos.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showContactWithoutNumber() {
        Toast.makeText(getContext(), "Esse contato não possui um telefone cadastrado", Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateList(final int position, Contact contact) {
        ContactView view = null;
        switch (position) {
            case 0:
                view = contact1;
                break;
            case 1:
                view = contact2;
                break;
            case 2:
                view = contact3;
                break;

        }
        if (view != null) {
            loadContact(view, contact, position);
        }
    }

    @Override
    public void onClickPickContacts(int idContactList) {
        networkPresenter.pickContact(this, Integer.toString(idContactList));
    }


}
