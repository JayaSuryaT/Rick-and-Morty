package com.jayasuryat.characterlist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.jayasuryat.base.anim.impl.AlphaAnim
import com.jayasuryat.base.anim.AnimHelper
import com.jayasuryat.base.anim.impl.CircleRevealHelper
import com.jayasuryat.base.anim.impl.TranslateAnim
import com.jayasuryat.base.arch.BaseAbsFragment
import com.jayasuryat.base.show
import com.jayasuryat.base.shrinkOnClick
import com.jayasuryat.characterlist.NavigateBack
import com.jayasuryat.characterlist.OpenCharacter
import com.jayasuryat.characterlist.databinding.FragmentCharacterListBinding
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.atomic.AtomicBoolean


@AndroidEntryPoint
class CharacterListFragment : BaseAbsFragment<CharacterListViewModel,
        FragmentCharacterListBinding>(FragmentCharacterListBinding::inflate) {

    private val hasLanded: AtomicBoolean = AtomicBoolean(false)

    private val characterListAdapter: CharactersListAdapter by lazy {
        CharactersListAdapter(::openCharacter)
    }

    override val viewModel: CharacterListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postponeEnterTransition()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setupViews(): FragmentCharacterListBinding.() -> Unit = {

        //binding.clRoot.post(::revealRoot)
        binding.clRoot.post(::handleAnimation)

        ivBack.shrinkOnClick(::navigateBack)

        rvCharactersList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = characterListAdapter
        }
    }

    override fun setupObservers(): CharacterListViewModel.() -> Unit = {

        obsCharactersList.observe(viewLifecycleOwner) { characters ->
            characterListAdapter.submitList(characters)
            binding.rvCharactersList.show()
            (view?.parent as? ViewGroup)
                ?.doOnPreDraw { startPostponedEnterTransition() }
        }
    }

    private fun revealRoot() {

        val argsX = arguments?.get("x")?.toString()?.toIntOrNull() ?: 0
        val argsY = arguments?.get("y")?.toString()?.toIntOrNull() ?: 0

        val hasLanded = hasLanded.get()

        val startX = if (hasLanded) 64 else argsX
        val startY = if (hasLanded) 154 else argsY

        CircleRevealHelper.Builder(binding.clRoot)
            .setStartPoint(startX.toDouble(), startY.toDouble())
            .setFarthestPointFromStartAsEnd()
            .build()
            .animation
            .start()
    }

    private fun handleAnimation() {

        fun defaultAnimation() {

            AnimHelper.create {
                addAnim {
                    AlphaAnim.builder()
                        .intermediateSteps(0f)
                        .build(binding.ivBack, binding.rvCharactersList)
                }
                addAnim {
                    TranslateAnim.builder()
                        .fromHorizontalDelta(-100f)
                        .toCurrentPosition()
                        .build(binding.ivBack)
                }
                addAnim {
                    TranslateAnim.builder()
                        .fromVerticalDelta(200f)
                        .toCurrentPosition()
                        .build(binding.rvCharactersList)
                }
            }.start()
        }

        fun backAnimation() {

            AnimHelper.create {
                addAnim {
                    AlphaAnim.builder()
                        .intermediateSteps(0f)
                        .build(binding.rvCharactersList)
                }
                addAnim {
                    TranslateAnim.builder()
                        .fromVerticalDelta(-164f)
                        .toCurrentPosition()
                        .build(binding.rvCharactersList)
                }
            }.start()
        }

        if (hasLanded.compareAndSet(false, true)) defaultAnimation()
        else backAnimation()
    }

    private fun navigateBack() = EventBus.getDefault().post(NavigateBack)

    private fun openCharacter(character: CharacterDef, image: View, name: View, container: View) {

        val extras = FragmentNavigatorExtras(
            image to "characterAvatar",
            name to "characterName",
            container to "characterContainer",
        )

        EventBus.getDefault()
            .post(OpenCharacter(characterId = character.id, extras))
    }
}