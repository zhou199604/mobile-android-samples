package com.carto.advanced.kotlin.sections.styles

import android.os.Bundle
import com.carto.advanced.kotlin.components.popupcontent.languagepopupcontent.LanguageCell
import com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent.StylePopupContentSection
import com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent.StylePopupContentSectionItem
import com.carto.advanced.kotlin.model.Languages
import com.carto.advanced.kotlin.sections.base.activities.BaseActivity

/**
 * Created by aareundo on 30/06/2017.
 */
class StyleChoiceActivity : BaseActivity() {

    var contentView: StyleChoiceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = StyleChoiceView(this)
        setContentView(contentView)

        contentView?.languageContent?.addItems(Languages.list)
        contentView?.baseMapContent?.highlightDefault()
    }

    override fun onResume() {
        super.onResume()
        contentView?.addListeners()

        contentView?.baseMapContent?.getItems()?.forEach {
            section: StylePopupContentSection ->
            section.list.forEach {
                item: StylePopupContentSectionItem ->
                item.setOnClickListener {

                    if (contentView?.baseMapContent?.previous != null) {
                        contentView?.baseMapContent?.previous!!.normalize()
                    }

                    item.highlight()
                    contentView?.baseMapContent?.previous = item

                    contentView?.popup?.hide()
                    contentView?.updateBaseLayer(item.label.text as String, section.source!!)
                }
            }
        }

        contentView?.languageContent?.list?.setOnItemClickListener { _, view, _, _ ->
            run {
                contentView?.popup?.hide()
                val cell = view as LanguageCell
                contentView?.updateMapLanguage(cell.item!!.value)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        contentView?.removeListeners()

        contentView?.baseMapContent?.getItems()?.forEach {
            section: StylePopupContentSection ->
            section.list.forEach {
                item: StylePopupContentSectionItem ->
                item.setOnClickListener(null)
            }
        }

        contentView?.languageContent?.list?.onItemClickListener = null
    }
}