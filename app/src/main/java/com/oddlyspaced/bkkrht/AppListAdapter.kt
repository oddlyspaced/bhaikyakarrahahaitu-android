package com.oddlyspaced.bkkrht

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oddlyspaced.bkkrht.databinding.ItemAppBinding

class AppListAdapter(private val context: Context) : RecyclerView.Adapter<AppListAdapter.AppViewHolder>()  {

    private val appList = arrayListOf<ApplicationInfo>()
    private val packageManager: PackageManager = context.packageManager

    init {
        context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA).forEach { info ->
            if (info.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                appList.add(info)
            }
        }
    }

    inner class AppViewHolder(private val item: ItemAppBinding) : RecyclerView.ViewHolder(item.root) {
        fun bind(data: ApplicationInfo) {
            item.txPkg.text = data.packageName
            item.txApp.text = data.loadLabel(packageManager)
            item.imgIcon.setImageDrawable(data.loadIcon(packageManager))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder(ItemAppBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(appList[position])
    }

    override fun getItemCount(): Int {
        return appList.size
    }
}