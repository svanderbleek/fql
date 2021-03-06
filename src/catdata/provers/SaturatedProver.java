package catdata.provers;

import catdata.Ctx;
import catdata.Triple;
import catdata.Util;
import catdata.provers.KBExp.KBApp;

import java.util.*;
import java.util.stream.Collectors;

//TODO aql try out saturated prover
public class SaturatedProver<T, C, V> extends DPKB<T, C, V> {


	public final Ctx<C, Map<List<C>, C>> map = new Ctx<>();

	public SaturatedProver(KBTheory<T,C,V> th) throws InterruptedException {
		super(th.tys, th.syms, th.eqs);

		for (Triple<Map<V, T>, KBExp<C, V>, KBExp<C, V>> eq : theory) {
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			if (!eq.first.isEmpty()) { // do this in check method?
				throw new RuntimeException("Saturated method can not work with universal quantification"); 
			}

			if (eq.second.isVar) {
				throw new RuntimeException(eq.second + " is a variable and not an application as expected");
			} else if (eq.third.isVar) {
				throw new RuntimeException(eq.third + " is a variable and not a constant as expected");
			} else if (!eq.third.getApp().args.isEmpty()) {
				throw new RuntimeException(eq.third + " is not a constant as expected");				
			}
			KBApp<C, V> lhs = eq.second.getApp();
			KBApp<C, V> rhs = eq.third.getApp();

			List<C> args = new LinkedList<>();
			for (KBExp<C, V> arg : lhs.args) {
				if (arg.isVar || !arg.getApp().args.isEmpty()) {
					throw new RuntimeException(arg + " is a variable and not a constant as expected");
				}
				args.add(arg.getApp().f);
			}
			if (!map.containsKey(lhs.f)) {
				map.put(lhs.f, new HashMap<>());
			}

			Util.putSafely(map.get(lhs.f), args, rhs.f);
		}

	}

	
	@Override
	public boolean eq(Map<V, T> ctx, KBExp<C, V> lhs, KBExp<C, V> rhs) {
		if (!ctx.isEmpty()) {
			throw new RuntimeException("Saturated prover only works on ground equations");
		}
		return nf(ctx, lhs).equals(nf(ctx, rhs));
	}

	@Override
	public boolean hasNFs() {
		return true;
	}

	@Override
	public KBExp<C, V> nf(Map<V, T> ctx, KBExp<C, V> term) {
		if (term.isVar) {
			return term;
		}
		List<KBExp<C, V>> args = term.getApp().args.stream().map(x -> nf(ctx, x)).collect(Collectors.toList());
		Map<List<C>, C> m = map.get(term.getApp().f);
		if (m == null) {
			return new KBApp<>(term.getApp().f, args);
		}
		List<C> args0 = new LinkedList<>();
		for (KBExp<C, V> arg : args) {
			if (!arg.isVar && arg.getApp().args.isEmpty()) {
				args0.add(arg.getApp().f);
			} else {
				return new KBApp<>(term.getApp().f, args);
			}
		}
		C c = m.get(args0);
		if (c == null) {
			return new KBApp<>(term.getApp().f, args);
		}
		return new KBApp<>(c, Collections.emptyList());
	}

	@Override
	public String toString() {
		return "Saturated prover";
	}



}
