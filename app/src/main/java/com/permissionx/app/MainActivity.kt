package com.permissionx.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.permissionx.luckl.PermissionX


class MainActivity : AppCompatActivity() {

    private var mTextView:TextView? = null

    //　递归查找显示标题文本的TextView控件，如果找到，将该TextView对象保存在mTextView变量中
    private fun getTextView(viewGroup: ViewGroup){
        //　获取当前容器视图中子视图的个数
        val count = viewGroup.childCount
        //扫描所有子视图
        for (i in 0 until count){
            val view = viewGroup.getChildAt(i)
            if (view is ViewGroup){
                getTextView(view as ViewGroup)
            }else if (view is View){
                if (view is TextView && mTextView == null){
                    mTextView = view
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val callBtn = findViewById<Button>(R.id.btn_call)
        val calculatorBtn = findViewById<Button>(R.id.btn_open_calculator)
        val window = window
        //获取窗口的顶层视图对象
        val viewGroup = window.decorView as ViewGroup
        getTextView(viewGroup)
        if (mTextView!= null){
            mTextView!!.text = "标题文本已变化"
        }

        callBtn.setOnClickListener {
            PermissionX.request(this,Manifest.permission.CALL_PHONE){ allGranted,deniedList ->
                if (allGranted){
                    call()
                } else {
                    Toast.makeText(this,"You denied $deniedList",Toast.LENGTH_SHORT).show()
                }
            }
        }

        calculatorBtn.setOnClickListener {
//            openOperator()
            open()
        }
    }

    private fun call(){
        try {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:10086")
            startActivity(intent)
        } catch (e:SecurityException){
            e.printStackTrace()
        }
    }

    fun requestManyPermissionExample(){
        PermissionX.request(this,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS) { allGranted, deniedList ->
            if (allGranted) {
                Toast.makeText(this, "All permissions are granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "You denied $deniedList", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openOperator(){
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_APP_CALCULATOR)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        startActivity(intent)
    }

    fun open(){
        var calculatorPackage: String? = null
        val pm: PackageManager = this.packageManager
        // 查询所有已经安装的应用程序
        // 查询所有已经安装的应用程序
        val appInfos =
            pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES) // GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的

        for (applicationInfo in appInfos) {
            val packageName = applicationInfo.packageName
            Log.d("APP","$packageName")
            if (packageName.contains("weathermvvm")) {
                calculatorPackage = packageName
                break
            }
        }
        val intent: Intent? =
            calculatorPackage?.let { packageManager.getLaunchIntentForPackage(it) }
        startActivity(intent)
    }
}