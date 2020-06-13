package com.example.aulafirebase06052020myproject

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.aulafirebase06052020myproject.Model.Armas
import com.example.aulafirebase06052020myproject.Model.Pais
import com.example.aulafirebase06052020myproject.apiservice.ApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_cadastro_atualizacao.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroAtualizacaoActivity : AppCompatActivity() {

    //private lateinit var firebaseStorage: FirebaseStorage
    //private lateinit var storageReference: StorageReference

    var countryNames_mutable = mutableListOf<String?>("Origem (Exemplo: URSS)")
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_atualizacao)

        btnListaArmas.setOnClickListener {
            val intent = Intent(this, ListaArmasActivity::class.java)
            startActivity(intent)
        }
        butlogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
        }

        //PEGANDO DADOS API
        val call = ApiClient.getPaisesService().all()

        call.enqueue(
            object : Callback<List<Pais>> {
                override fun onFailure(
                    call: Call<List<Pais>>,
                    t: Throwable) {
                    Log.d("Retrofit", t.message)
                }

                override fun onResponse(call: Call<List<Pais>>, response: Response<List<Pais>>)
                {
                    val pais = response.body()
                    pais?.forEach{
                        countryNames_mutable.add(it.name)
//                        Log.d("Retrofit", it.name)
//                        Log.d("Retrofit", "${countryNames_mutable}")
                    }
                    //CARREGAR ADAPTER DO SPINNER
                    val arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, countryNames_mutable)

                    //incluir arrayadapter no spinner
                    spinner.adapter = arrayAdapter
                }
            })


        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                spinnerResult.text = countryNames[position]
            }
        }

        //Instância do FIRESTORE, ela faz a conexão com a base
        firebaseFirestore = FirebaseFirestore.getInstance()

        //Apontar mas ainda não conectando para a coleção que estamos criando em nossa base
        val collection = firebaseFirestore.collection("armas")

        btnCadastrar.setOnClickListener {
            if (edtTxtNome.text.isNullOrEmpty() || edtTxtTipo.text.isNullOrEmpty() || spinner.selectedItem.toString() =="Origem (Exemplo: URSS)"){
                Toast.makeText(this, "Preencha todos os campos e escolha um país de origem.", Toast.LENGTH_LONG).show()
            }
            else{
                val document = collection.document(edtTxtNome.text.toString())
                val task = document.set(
                    Armas(
                        edtTxtNome.text.toString(),
                        edtTxtTipo.text.toString(),
                        spinner.selectedItem.toString()
//                        edtTxtOrigem.text.toString()
                    )
                )
                task.addOnCompleteListener {
                    Toast.makeText(this, "Tarefa realizada sem erros.", Toast.LENGTH_SHORT).show()
                }
                task.addOnFailureListener {
                    Toast.makeText(this, "Erro em realizar tarefa.", Toast.LENGTH_LONG).show()
                }
                task.addOnSuccessListener {
                    Toast.makeText(this, "Arma cadastrada ou atualizada com SUCESSO.", Toast.LENGTH_LONG).show()
                }
            }
            }
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.atualizar_test, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item3 -> {
                Toast.makeText(this, "Câmera ainda em implementação.", Toast.LENGTH_SHORT).show()
                val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                if (permission != PackageManager.PERMISSION_GRANTED){
                    Log.d("Permissao", "Permission denied")
                }
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)){
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Permissao para acessar a camera é necessaria")
                    builder.setTitle("Permissao requerida")
                }
            }
        }

        return true
    }

        //val user = User("Thiago", "Aguiar", "Rio de Janeiro", "thiago@aguiR.com")
//        val arma_m4a1 = Armas("M4A1", "Carabina", "USA")
//        val arma_ak47 = Armas("AK47", "Rifle Automático", "URSS")
//        val arma_galil = Armas("Galil AR", "Fuzil de assalto", "Israel")

        //Add um documento com ID genérico
//         val task = collection.add(arma_m4a1)

        //Apontar documento com nome epecífico e usar Set para alimentar o nome
//        val document = collection.document(arma_galil.nome.toString())
//        val task = document.set(arma_galil)

        //addOn's para setar e alimentar
//        task.addOnCompleteListener {
//                Toast.makeText(this, "Tarefa realizada sem erros.", Toast.LENGTH_SHORT).show()
//        }
//        task.addOnFailureListener {
//            Toast.makeText(this, "Erro em realizar tarefa.", Toast.LENGTH_LONG).show()
//        }
//        task.addOnSuccessListener {
//                Toast.makeText(this, "Arma cadastrada ou atualizada com SUCESSO.", Toast.LENGTH_LONG).show()
//            }
//        }

        //Ler e acessar dados, COM METODO GET que se existir, irá retornar o documento
//        val document = collection.document("KszUPU4qAKAM4E5O82Ea")
//        val task = document.get()

        //addOn's para ler um documento
//        task.addOnCompleteListener {
//            Toast.makeText(this, "Tarefa completa.", Toast.LENGTH_SHORT).show()
//        }
//        task.addOnFailureListener {
//            Toast.makeText(this, "Erro em realizar leitura.", Toast.LENGTH_LONG).show()
//        }
//        task.addOnSuccessListener {
//            if (it != null) {
//                //O toObject é um método que quando passado um tipo de classe (Armas) tenta fazer a adequação do it: DocumentSnapshot! que vem do Firebase para um objeto da classe passada.
//                var armas_dados = it.toObject(Armas::class.java)
//                Log.i("Documento", armas_dados.toString())
//                Toast.makeText(this, "Leitura feita com SUCESSO.", Toast.LENGTH_LONG).show()
//            }
//        }
//    }

        //Ler e acessar dados, COM METODO GET que se existir, irá retornar uma collection
//        val task = collection.get()
//
//        //addOn's para ler um documento
//        task.addOnCompleteListener {
//            Toast.makeText(this, "Tarefa completa.", Toast.LENGTH_SHORT).show()
//        }
//        task.addOnFailureListener {
//            Toast.makeText(this, "Erro em realizar leitura.", Toast.LENGTH_LONG).show()
//        }
//        task.addOnSuccessListener {
//            if (it != null) {
//                //Neste caso o toObjects irá retornar uma lista
//                //armas_dados_collection é uma lista comvertida para a classe Armas
//                var armas_dados_collection = it.toObjects(Armas::class.java)
//                armas_dados_collection.forEach {
//                    println(it)
//                    //Log.i("Documento", armas_dados_collection.toString())
//                }
//                Toast.makeText(this, "Leitura feita com SUCESSO.", Toast.LENGTH_LONG).show()
//            }
//        }

        //Setup Storage Download
        /*
        //Conexao Instancia FirebaseStorage
        firebaseStorage = FirebaseStorage.getInstance()

        //Diretorio raiz tipo C:/
        storageReference = firebaseStorage.reference

        //C:/js.png
        val arq = storageReference.child("js.png")

        val task = arq.getBytes(1024*1024)

        task.addOnSuccessListener {
            var bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            imgVwDown.setImageBitmap(bitmap)
        }.addOnFailureListener {
            Toast.makeText(
                this,
                it.message,
                Toast.LENGTH_SHORT
            ).show()
        }*/
    }



