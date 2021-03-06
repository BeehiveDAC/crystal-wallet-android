package cy.agorise.crystalwallet.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.CryptoCurrencyEquivalence;
import cy.agorise.crystalwallet.models.CryptoNetBalance;

/**
 * Created by Henry Varona on 27/9/2017.
 */

public class CryptoCurrencyEquivalenceListViewModel extends AndroidViewModel {

    private List<CryptoCurrencyEquivalence> cryptoCurrencyEquivalenceList;
    private CrystalDatabase db;

    public CryptoCurrencyEquivalenceListViewModel(Application application) {
        super(application);
        this.db = CrystalDatabase.getAppDatabase(application.getApplicationContext());
    }

    public void init(){
        this.cryptoCurrencyEquivalenceList = this.db.cryptoCurrencyEquivalenceDao().getAll();
    }

    public List<CryptoCurrencyEquivalence> getCryptoCurrencyEquivalenceList(){
        return this.cryptoCurrencyEquivalenceList;
    }
}
